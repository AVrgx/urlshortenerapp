package ru.mypetproject.urlshortenerapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortenRequest {
    @NotBlank(message = "Запрос не должен быть пустым")
    @Size(max = 2048, message = "Длинна не должна превышать 2048 символов")
    @Pattern(regexp = "^(https?://).*", message = "URL должен начинаться с http:// или https://")
    private String url;

    private Integer ttlDays;
}
