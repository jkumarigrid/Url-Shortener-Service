package com.griddynamics.urlshortener.controller;

import com.griddynamics.urlshortener.domain.UrlMapping;
import com.griddynamics.urlshortener.service.UrlService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlController.class)
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UrlService urlService;

    @Test
    void shouldShortenUrl() throws Exception {
        UrlMapping mapping = new UrlMapping(
                "https://www.google.com", "abc12345");

        when(urlService.shortenUrl("https://www.google.com"))
                .thenReturn(mapping);

        mockMvc.perform(post("/api/urls")
                        .contentType("application/json")
                        .content("""
                                {"url":"https://www.google.com"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl")
                        .value("https://www.google.com"))
                .andExpect(jsonPath("$.shortCode").value("abc12345"))
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    void shouldRedirectToOriginalUrl() throws Exception {
        UrlMapping mapping = new UrlMapping(
                "https://www.google.com", "abc12345");

        when(urlService.getOriginalUrl("abc12345"))
                .thenReturn(Optional.of(mapping));

        mockMvc.perform(get("/abc12345"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string(
                        "Location", "https://www.google.com"));
    }

    @Test
    void shouldReturnBadRequestForInvalidUrl() throws Exception {
        mockMvc.perform(post("/api/urls")
                        .contentType("application/json")
                        .content("""
                                {"url":"not-a-url"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForEmptyUrl() throws Exception {
        mockMvc.perform(post("/api/urls")
                        .contentType("application/json")
                        .content("""
                                {"url":""}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundForUnknownShortCode() throws Exception {
        when(urlService.getOriginalUrl("unknown123"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/unknown123"))
                .andExpect(status().isNotFound());
    }
}