package com.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.entity.IndividualImageEntity;
import com.backend.service.IndividualImageService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/individuals/{species_cd}/{id}/images")
public class IndividualImageController {

    private final IndividualImageService individualImageService;

    public IndividualImageController(IndividualImageService individualImageService) {
        this.individualImageService = individualImageService;
    }

    @GetMapping
    public ResponseEntity<List<IndividualImageEntity>> getImages(
        @PathVariable("species_cd") String speciesCd,
        @PathVariable("id") String individualId
    ) {
        return ResponseEntity.ok(individualImageService.getImages(speciesCd, individualId));
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<IndividualImageEntity> uploadImage(
        @PathVariable("species_cd") String speciesCd,
        @PathVariable("id") String individualId,
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "isPrimary", required = false) Boolean isPrimary,
        @RequestParam(value = "sortOrder", required = false) Integer sortOrder,
        @RequestParam(value = "createdBy", required = false) String createdBy
    ) {
        IndividualImageEntity created = individualImageService.uploadImage(
            speciesCd,
            individualId,
            file,
            isPrimary,
            sortOrder,
            createdBy
        );
        return ResponseEntity.ok(created);
    }

    @PutMapping(value = "/{image_id}", consumes = "multipart/form-data")
    public ResponseEntity<IndividualImageEntity> replaceImage(
        @PathVariable("species_cd") String speciesCd,
        @PathVariable("id") String individualId,
        @PathVariable("image_id") Long imageId,
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "isPrimary", required = false) Boolean isPrimary,
        @RequestParam(value = "updatedBy", required = false) String updatedBy
    ) {
        IndividualImageEntity updated = individualImageService.replaceImage(
            speciesCd,
            individualId,
            imageId,
            file,
            isPrimary,
            updatedBy
        );
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{image_id}")
    public ResponseEntity<Void> deleteImage(
        @PathVariable("species_cd") String speciesCd,
        @PathVariable("id") String individualId,
        @PathVariable("image_id") Long imageId
    ) {
        individualImageService.deleteImage(speciesCd, individualId, imageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{image_id}/primary")
    public ResponseEntity<Void> setPrimaryImage(
        @PathVariable("species_cd") String speciesCd,
        @PathVariable("id") String individualId,
        @PathVariable("image_id") Long imageId,
        @RequestParam(value = "updatedBy", required = false) String updatedBy
    ) {
        individualImageService.setPrimaryImage(speciesCd, individualId, imageId, updatedBy);
        return ResponseEntity.ok().build();
    }
}
