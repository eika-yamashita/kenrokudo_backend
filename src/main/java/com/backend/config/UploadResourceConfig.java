package com.backend.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadResourceConfig implements WebMvcConfigurer {

    private final String uploadBaseDir;

    public UploadResourceConfig(@Value("${app.upload.base-dir}") String uploadBaseDir) {
        this.uploadBaseDir = uploadBaseDir;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = Paths.get(uploadBaseDir).toAbsolutePath().normalize().toUri().toString();
        registry
            .addResourceHandler("/media/**")
            .addResourceLocations(absolutePath + "/");
    }
}
