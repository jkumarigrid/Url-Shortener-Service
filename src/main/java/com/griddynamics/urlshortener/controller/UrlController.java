package com.griddynamics.urlshortener.controller;

import com.griddynamics.urlshortener.domain.UrlMapping;
import com.griddynamics.urlshortener.dto.ShortenUrlResponse;
import com.griddynamics.urlshortener.service.UrlService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/urls")
    public ShortenUrlResponse shortenUrl(@RequestBody UrlRequest request) {
        String url = request.url();

        if (url == null || url.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "URL is required");
        }

        try {
            URI uri = URI.create(url);

            if (uri.getHost() == null ||
                    !("http".equals(uri.getScheme())
                            || "https".equals(uri.getScheme()))) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid URL");
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid URL");
        }

        UrlMapping mapping = urlService.shortenUrl(url);

        return new ShortenUrlResponse(
                mapping.getOriginalUrl(),
                mapping.getShortCode()
        );
    }

    @GetMapping("/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode) {
        UrlMapping mapping = urlService.getOriginalUrl(shortCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Short URL not found"));

        return new RedirectView(mapping.getOriginalUrl());
    }

    public record UrlRequest(String url) {}
}