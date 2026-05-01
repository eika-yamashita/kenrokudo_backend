package com.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.api.IndividualImagesApi;
import com.backend.entity.IndividualImageEntity;
import com.backend.service.IndividualImageService;

@RestController
@CrossOrigin(origins = "*")
public class IndividualImageController implements IndividualImagesApi {

    private final IndividualImageService individualImageService;

    public IndividualImageController(IndividualImageService individualImageService) {
        this.individualImageService = individualImageService;
    }

    @Override
    public ResponseEntity<List<IndividualImageEntity>> getImages(
        String speciesId,
        String individualId
    ) {
        return ResponseEntity.ok(individualImageService.getImages(speciesId, individualId));
    }

    @Override
    public ResponseEntity<IndividualImageEntity> uploadImage(
        String speciesId,
        String individualId,
        MultipartFile file,
        Boolean isPrimary,
        Integer sortOrder,
        String createdBy
    ) {
        IndividualImageEntity created = individualImageService.uploadImage(
            speciesId,
            individualId,
            file,
            isPrimary,
            sortOrder,
            createdBy
        );
        return ResponseEntity.ok(created);
    }

    @Override
    public ResponseEntity<IndividualImageEntity> replaceImage(
        String speciesId,
        String individualId,
        Long imageId,
        MultipartFile file,
        Boolean isPrimary,
        String updatedBy
    ) {
        IndividualImageEntity updated = individualImageService.replaceImage(
            speciesId,
            individualId,
            imageId,
            file,
            isPrimary,
            updatedBy
        );
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> deleteImage(
        String speciesId,
        String individualId,
        Long imageId
    ) {
        individualImageService.deleteImage(speciesId, individualId, imageId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> setPrimaryImage(
        String speciesId,
        String individualId,
        Long imageId,
        String updatedBy
    ) {
        individualImageService.setPrimaryImage(speciesId, individualId, imageId, updatedBy);
        return ResponseEntity.ok().build();
    }
}
