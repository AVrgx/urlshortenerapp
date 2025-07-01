package ru.mypetproject.urlshortenerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@Tag(name = "URL Shortener API", description = "API для сокращения URL и аналитики")
public class UrlShortenerController {
    private final UrlShortenerService service;

    @Operation(
            summary = "Сократить URL",
            description = "Создает короткую версию длинного URL с установленным TTL",
            responses = {
                    @ApiResponse(responseCode = "200", description = "URL успешно сокращен"),
                    @ApiResponse(responseCode = "400", description = "Некорректный URL")
            }
    )
    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@Valid @RequestBody ShortenRequest request) {
        String shortKey = service.shortenUrl(request.getUrl(), request.getTtlDays());
        return ResponseEntity.ok(shortKey);
    }

    @Operation(
            summary = "Получить все ссылки",
            description = "Возвращает список всех сокращенных URL с их метаданными",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен")
            }
    )
    @GetMapping("/getall")
    public ResponseEntity<List<ShortUrl>> getAll() {
        List<ShortUrl> urls = service.getAllUrls();
        return ResponseEntity.ok(urls);
    }

    @Operation(
            summary = "Перейти по короткой ссылке",
            description = "Перенаправляет на оригинальный URL и регистрирует клик",
            parameters = {
                    @Parameter(name = "code", description = "Короткий код ссылки", example = "abc123")
            },
            responses = {
                    @ApiResponse(responseCode = "302", description = "Редирект на оригинальный URL"),
                    @ApiResponse(responseCode = "404", description = "Ссылка не найдена или просрочена")
            }
    )
    @GetMapping("{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code, HttpServletRequest request) {
        // Позволяет получить информацию о клиенте (IP, браузер)
        return service.getOriginalUrl(code, request)
                .map(originalUrl-> ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                        .<Void>build())
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(
            summary = "Удалить ссылку",
            description = "Удаляет сокращенную ссылку по её ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Ссылка успешно удалена"),
                    @ApiResponse(responseCode = "404", description = "Ссылка не найдена")
            }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Статистика ссылки",
            description = "Возвращает статистику переходов по короткой ссылке",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статистика получена"),
                    @ApiResponse(responseCode = "404", description = "Ссылка не найдена")
            }
    )
    @GetMapping("/{shortKey}/stats")
    public ResponseEntity<UrlStatsResponse> getStats(@PathVariable String shortKey) {
        return service.getStats(shortKey)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Детализация кликов",
            description = "Возвращает подробную информацию о каждом переходе по ссылке",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные получены"),
                    @ApiResponse(responseCode = "404", description = "Ссылка не найдена")
            }
    )
    @GetMapping("/{shortKey}/clicks")
    public ResponseEntity<List<ClickInfo>> getClickDetails(@PathVariable String shortKey) {
        return ResponseEntity.ok(service.getClickDetails(shortKey));
    }

}
