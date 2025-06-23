package ru.mypetproject.urlshortenerapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mypetproject.urlshortenerapp.dto.ShortenRequest;
import ru.mypetproject.urlshortenerapp.model.ShortUrl;
import ru.mypetproject.urlshortenerapp.service.UrlShortenerService;

import java.io.InputStream;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UrlShortenerController {
    private final UrlShortenerService service;

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@Valid @RequestBody ShortenRequest request) {
        String shortKey = service.ShortenUrl(request.getUrl());
        return ResponseEntity.ok(shortKey);
    }

    @GetMapping("{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        return service.getOriginalUrl(code)
                .map(originalUrl-> ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                        .<Void>build())
                .orElse(ResponseEntity.notFound().build());

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
