package com.griddynamics.urlshortener.service;

import com.griddynamics.urlshortener.domain.UrlMapping;
import com.griddynamics.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    @Test
    void shouldShortenUrl() {
        String originalUrl = "https://www.google.com";

        when(urlRepository.save(any(UrlMapping.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UrlMapping result = urlService.shortenUrl(originalUrl);

        assertEquals(originalUrl, result.getOriginalUrl());
        assertNotNull(result.getShortCode());
        assertEquals(8, result.getShortCode().length());

        verify(urlRepository).save(any(UrlMapping.class));
    }

    @Test
    void shouldFindOriginalUrlByShortCode() {
        UrlMapping mapping = new UrlMapping(
                "https://www.google.com", "abc12345");

        when(urlRepository.findByShortCode("abc12345"))
                .thenReturn(Optional.of(mapping));

        Optional<UrlMapping> result =
                urlService.getOriginalUrl("abc12345");

        assertTrue(result.isPresent());
        assertEquals("https://www.google.com",
                result.get().getOriginalUrl());
    }
}