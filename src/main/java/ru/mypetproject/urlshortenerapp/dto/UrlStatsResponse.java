package ru.mypetproject.urlshortenerapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UrlStatsResponse {
    private String shortKey;
    private String originalUrl;
    private int totalClicks;
    private LocalDateTime lastClickTime;
}
