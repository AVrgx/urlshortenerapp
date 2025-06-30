package ru.mypetproject.urlshortenerapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ClickInfo {
    private LocalDateTime clickedAt;
    private String ipAddress;
    private String deviceType;
}
