package ru.mypetproject.urlshortenerapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UrlClick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "short_url_id") // Связь с short_url
    private ShortUrl shortUrl; // К какой ссылке относится клик

    private LocalDateTime clickedAt;    // Время перехода
    private String ipAddress;          // IP пользователя
    private String userAgent;          // Браузер и ОС

    public UrlClick(ShortUrl shortUrl, String ipAddress, String userAgent) {
        this.shortUrl = shortUrl;
        this.clickedAt = LocalDateTime.now();
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }
}
