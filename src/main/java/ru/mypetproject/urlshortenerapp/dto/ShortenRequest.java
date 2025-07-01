package ru.mypetproject.urlshortenerapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Запрос на сокращение URL")
@Getter
@Setter
public class ShortenRequest {
    @Schema(
            description = "Оригинальный URL для сокращения",
            example = "https://example.com/very/long/url",
            requiredMode = Schema.RequiredMode.REQUIRED
    )

    @NotBlank(message = "Запрос не должен быть пустым")
    @Size(max = 2048, message = "Длинна не должна превышать 2048 символов")
    @Pattern(regexp = "^(https?://).*", message = "URL должен начинаться с http:// или https://")
    private String url;

    @Schema(
            description = "Срок жизни ссылки в днях (опционально)",
            example = "30",
            defaultValue = "30"
    )
    private Integer ttlDays;
}
