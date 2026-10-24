package com.griddynamics.urlshortener.controller;

import com.griddynamics.urlshortener.domain.UrlMapping;
import com.griddynamics.urlshortener.service.UrlService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/urls")
    public UrlMapping shortenUrl(@RequestBody UrlRequest request) {
        return urlService.shortenUrl(request.url());
    }

    @GetMapping("/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode) {
        UrlMapping mapping = urlService.getOriginalUrl(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

        return new RedirectView(mapping.getOriginalUrl());
    }

    public record UrlRequest(String url) {}
}