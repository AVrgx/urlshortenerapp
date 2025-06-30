package ru.mypetproject.urlshortenerapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mypetproject.urlshortenerapp.dto.ClickInfo;
import ru.mypetproject.urlshortenerapp.dto.ShortenRequest;
import ru.mypetproject.urlshortenerapp.dto.UrlStatsResponse;
import ru.mypetproject.urlshortenerapp.model.ShortUrl;
import ru.mypetproject.urlshortenerapp.service.UrlShortenerService;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UrlShortenerController {
    private final UrlShortenerService service;

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@Valid @RequestBody ShortenRequest request) {
        String shortKey = service.ShortenUrl(request.getUrl(), request.getTtlDays());
        return ResponseEntity.ok(shortKey);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<ShortUrl>> getAll() {
        List<ShortUrl> urls = service.getAllUrls();
        return ResponseEntity.ok(urls);
    }

    @GetMapping("{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code, HttpServletRequest request) {
        // Позволяет получить информацию о клиенте (IP, браузер)
        return service.getOriginalUrl(code, request)
                .map(originalUrl-> ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                        .<Void>build())
                .orElse(ResponseEntity.notFound().build());

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{shortKey}/stats")
    public ResponseEntity<UrlStatsResponse> getStats(@PathVariable String shortKey) {
        return service.getStats(shortKey)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/{shortKey}/clicks")
    public ResponseEntity<List<ClickInfo>> getClickDetails(@PathVariable String shortKey) {
        return ResponseEntity.ok(service.getClickDetails(shortKey));
    }

}
