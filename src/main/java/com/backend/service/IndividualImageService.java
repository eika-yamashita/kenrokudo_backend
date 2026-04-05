package com.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.backend.entity.IndividualEntity;
import com.backend.entity.IndividualImageEntity;
import com.backend.mapper.IndividualImageMapper;
import com.backend.mapper.IndividualMapper;

@Service
public class IndividualImageService {

    private static final long MAX_FILE_SIZE = 5L * 1024L * 1024L;
    private static final int MAX_FILES_PER_INDIVIDUAL = 10;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
        "image/jpeg",
        "image/png",
        "image/webp"
    );
    private static final Map<String, String> CONTENT_TYPE_EXTENSIONS = Map.of(
        "image/jpeg", ".jpg",
        "image/png", ".png",
        "image/webp", ".webp"
    );

    private final IndividualImageMapper individualImageMapper;
    private final IndividualMapper individualMapper;
    private final Path uploadBaseDir;

    public IndividualImageService(
        IndividualImageMapper individualImageMapper,
        IndividualMapper individualMapper,
        @Value("${app.upload.base-dir}") String uploadBaseDir
    ) {
        this.individualImageMapper = individualImageMapper;
        this.individualMapper = individualMapper;
        this.uploadBaseDir = Paths.get(uploadBaseDir).toAbsolutePath().normalize();
    }

    public List<IndividualImageEntity> getImages(String speciesId, String individualId) {
        ensureIndividualExists(speciesId, individualId);
        return individualImageMapper.findByIndividual(speciesId, individualId);
    }

    public IndividualImageEntity uploadImage(
        String speciesId,
        String individualId,
        MultipartFile file,
        Boolean isPrimary,
        Integer sortOrder,
        String createdBy
    ) {
        ensureIndividualExists(speciesId, individualId);
        validateUpload(file);

        int existingCount = individualImageMapper.countByIndividual(speciesId, individualId);
        if (existingCount >= MAX_FILES_PER_INDIVIDUAL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "画像は最大10件までです");
        }

        String extension = resolveExtension(file);
        Long imageId = individualImageMapper.nextImageId();
        String fileName = buildImageFileName(speciesId, individualId, imageId, extension);
        String relativePath = Paths.get("individuals", speciesId, individualId, fileName).toString().replace('\\', '/');
        Path destination = uploadBaseDir.resolve(relativePath).normalize();
        ensureDestination(destination);
        copyFile(file, destination);

        boolean shouldBePrimary = shouldBePrimary(speciesId, individualId, isPrimary);
        if (shouldBePrimary) {
            individualImageMapper.clearPrimaryByIndividual(speciesId, individualId);
        }

        IndividualImageEntity image = new IndividualImageEntity();
        image.setImageId(imageId);
        image.setSpeciesId(speciesId);
        image.setIndividualId(individualId);
        image.setStoragePath(relativePath);
        image.setPublicUrl(buildPublicUrl(relativePath));
        image.setFileName(fileName);
        image.setContentType(file.getContentType());
        image.setFileSize(file.getSize());
        image.setSortOrder(sortOrder != null ? sortOrder : individualImageMapper.nextSortOrder(speciesId, individualId));
        image.setIsPrimary(shouldBePrimary);
        image.setCreatedBy(createdBy);

        individualImageMapper.insert(image);
        IndividualImageEntity created = individualImageMapper.findByStoragePath(relativePath);
        if (created == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "image creation result could not be loaded");
        }
        return created;
    }

    public IndividualImageEntity replaceImage(
        String speciesId,
        String individualId,
        Long imageId,
        MultipartFile file,
        Boolean isPrimary,
        String updatedBy
    ) {
        validateUpload(file);
        IndividualImageEntity existing = getAndValidateImage(speciesId, individualId, imageId);

        String extension = resolveExtension(file);
        String newFileName = buildImageFileName(speciesId, individualId, imageId, extension);
        String relativePath = Paths.get("individuals", speciesId, individualId, newFileName).toString().replace('\\', '/');
        Path destination = uploadBaseDir.resolve(relativePath).normalize();
        ensureDestination(destination);
        copyFile(file, destination);

        boolean finalPrimary = existing.getIsPrimary();
        if (isPrimary != null) {
            finalPrimary = isPrimary;
        }
        if (finalPrimary) {
            individualImageMapper.clearPrimaryByIndividual(speciesId, individualId);
        }

        individualImageMapper.updateImageFile(
            imageId,
            relativePath,
            buildPublicUrl(relativePath),
            newFileName,
            file.getContentType(),
            file.getSize(),
            finalPrimary,
            updatedBy
        );

        deleteFileQuietly(uploadBaseDir.resolve(existing.getStoragePath()));
        return individualImageMapper.findByImageId(imageId);
    }

    public void deleteImage(String speciesId, String individualId, Long imageId) {
        IndividualImageEntity existing = getAndValidateImage(speciesId, individualId, imageId);
        individualImageMapper.deleteByImageId(imageId);
        deleteFileQuietly(uploadBaseDir.resolve(existing.getStoragePath()));

        if (Boolean.TRUE.equals(existing.getIsPrimary())) {
            List<IndividualImageEntity> remaining = individualImageMapper.findByIndividual(speciesId, individualId);
            if (!remaining.isEmpty()) {
                individualImageMapper.clearPrimaryByIndividual(speciesId, individualId);
                individualImageMapper.setPrimaryByImageId(remaining.get(0).getImageId(), null);
            }
        }
    }

    public void setPrimaryImage(String speciesId, String individualId, Long imageId, String updatedBy) {
        getAndValidateImage(speciesId, individualId, imageId);
        individualImageMapper.clearPrimaryByIndividual(speciesId, individualId);
        individualImageMapper.setPrimaryByImageId(imageId, updatedBy);
    }

    private IndividualImageEntity getAndValidateImage(String speciesId, String individualId, Long imageId) {
        IndividualImageEntity image = individualImageMapper.findByImageId(imageId);
        if (image == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "画像が見つかりません");
        }
        if (!speciesId.equals(image.getSpeciesId()) || !individualId.equals(image.getIndividualId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "個体キーと画像が一致しません");
        }
        return image;
    }

    private void ensureIndividualExists(String speciesId, String individualId) {
        IndividualEntity individual = individualMapper.findBySpeciesIdAndId(speciesId, individualId);
        if (individual == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "個体が見つかりません");
        }
    }

    private void validateUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "画像ファイルが指定されていません");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "画像サイズは5MB以下にしてください");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "許可されていない画像形式です");
        }
    }

    private String resolveExtension(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            String byMime = CONTENT_TYPE_EXTENSIONS.get(contentType.toLowerCase(Locale.ROOT));
            if (byMime != null) {
                return byMime;
            }
        }
        String original = file.getOriginalFilename();
        if (original != null) {
            int dot = original.lastIndexOf('.');
            if (dot >= 0) {
                return original.substring(dot).toLowerCase(Locale.ROOT);
            }
        }
        return ".bin";
    }

    private String buildImageFileName(String speciesId, String individualId, Long imageId, String extension) {
        return String.format(
            "%s_%s_%d%s",
            toSafeToken(speciesId),
            toSafeToken(individualId),
            imageId,
            extension
        );
    }

    private String toSafeToken(String value) {
        if (value == null || value.isBlank()) {
            return "unknown";
        }
        return value.replaceAll("[^A-Za-z0-9_-]", "_");
    }

    private boolean shouldBePrimary(String speciesId, String individualId, Boolean requestedPrimary) {
        if (Boolean.TRUE.equals(requestedPrimary)) {
            return true;
        }
        return individualImageMapper.countByIndividual(speciesId, individualId) == 0;
    }

    private String buildPublicUrl(String relativePath) {
        return "/media/" + relativePath;
    }

    private void ensureDestination(Path destination) {
        if (!destination.startsWith(uploadBaseDir)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "不正な保存先です");
        }

        try {
            Files.createDirectories(destination.getParent());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "保存先ディレクトリの作成に失敗しました", e);
        }
    }

    private void copyFile(MultipartFile file, Path destination) {
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "画像ファイルの保存に失敗しました", e);
        }
    }

    private void deleteFileQuietly(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }
}
