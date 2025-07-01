package ru.mypetproject.urlshortenerapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(description = "Статистика переходов по короткой ссылке")
public class UrlStatsResponse {
    @Schema(description = "Короткий ключ", example = "abc123")
    private String shortKey;
    @Schema(description = "Оригинальный URL", example = "example.com")
    private String originalUrl;
    @Schema(description = "Количество переходов", example = "42")
    private int totalClicks;
    @Schema(description = "Дата последнего перехода", example = "2025-06-30T14:25:00")
    private LocalDateTime lastClickTime;
}
