package com.griddynamics.urlshortener.service;

import com.griddynamics.urlshortener.domain.UrlMapping;
import com.griddynamics.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public UrlMapping shortenUrl(String originalUrl) {
        String shortCode;

        do {
            shortCode = UUID.randomUUID()
                    .toString()
                    .substring(0, 8);
        } while (urlRepository.findByShortCode(shortCode).isPresent());

        UrlMapping mapping = new UrlMapping(originalUrl, shortCode);
        return urlRepository.save(mapping);
    }

    public Optional<UrlMapping> getOriginalUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode);
    }
}