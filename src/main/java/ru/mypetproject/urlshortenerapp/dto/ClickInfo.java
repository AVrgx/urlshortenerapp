package ru.mypetproject.urlshortenerapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Информация о клике")
@Data
@AllArgsConstructor
public class ClickInfo {
    @Schema(description = "Время перехода", example = "2025-06-30T14:25:00")
    private LocalDateTime clickedAt;

    @Schema(description = "IP-адрес", example = "192.168.1.1")
    private String ipAddress;

    @Schema(description = "Тип устройства", example = "Mobile")
    private String deviceType;
}
