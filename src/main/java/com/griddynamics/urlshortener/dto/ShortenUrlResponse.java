package com.griddynamics.urlshortener.dto;

public record ShortenUrlResponse(
        String originalUrl,
        String shortCode
) {}