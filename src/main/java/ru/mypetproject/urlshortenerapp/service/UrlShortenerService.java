package ru.mypetproject.urlshortenerapp.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mypetproject.urlshortenerapp.dto.ClickInfo;
import ru.mypetproject.urlshortenerapp.dto.UrlStatsResponse;
import ru.mypetproject.urlshortenerapp.model.ShortUrl;
import ru.mypetproject.urlshortenerapp.model.UrlClick;
import ru.mypetproject.urlshortenerapp.repository.ShortUrlRepository;
import ru.mypetproject.urlshortenerapp.repository.UrlClickRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {
    private static final Logger log = LoggerFactory.getLogger(UrlShortenerService.class);
    private final ShortUrlRepository repository;
    private static final int SHORT_KEY_LENGTH = 6;
    private final UrlClickRepository urlClickRepository;
    private final ShortUrlRepository shortUrlRepository;
    @Value("${url.shortener.default-ttl-days}")
    private int defaultTtlDays;

    @Transactional
    public String shortenUrl(String originalUrl, Integer ttlDays) {
        Optional<ShortUrl> existingUrl = repository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get().getShortKey();
        } else {
            String shortKey;
            do {
                shortKey = generateShortKey(SHORT_KEY_LENGTH);
            } while (repository.existsByShortKey(shortKey));
           ShortUrl shortUrl = new ShortUrl();
           shortUrl.setShortKey(shortKey);
           shortUrl.setOriginalUrl(originalUrl);
           shortUrl.setCreatedAt(LocalDateTime.now());
           shortUrl.setExpiresAt(LocalDateTime.now().plusDays(defaultTtlDays));

            if (ttlDays != null) {
                shortUrl.setExpiresAt(LocalDateTime.now().plusDays(ttlDays));
            }
           repository.save(shortUrl);
           return shortKey;

        }
    }
    public List<ShortUrl> getAllUrls() {
        return repository.findAll();
    }

    public Optional<String> getOriginalUrl(String shortKey, HttpServletRequest request) {
        log.debug("Поиск ссылки для shortKey: {}", shortKey);
        Optional<ShortUrl> urlOpt = repository.findByShortKey(shortKey)
                .filter(url -> {
                    boolean valid = url.getExpiresAt() == null
                                    || LocalDateTime.now().isBefore(url.getExpiresAt());
                    if (!valid) {
                        log.debug("Ссылка просрочена: {}", url.getShortKey());
                    }
                    return valid;
                });

        if (urlOpt.isEmpty()) {
            log.debug("Ссылка не найдена или просрочена для key: {}", shortKey);
        }

        return urlOpt.map(url -> {
            trackClick(url, request);
            return url.getOriginalUrl();
        });
    }

    private void trackClick(ShortUrl shortUrl, HttpServletRequest request) {
        // Увеличиваем счётчик
        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        // Сохраняем детали клика
        UrlClick click = new UrlClick(
                shortUrl,
                request.getRemoteAddr(),               // IP
                request.getHeader("User-Agent")    // Browser
        );
        urlClickRepository.save(click);
    }
    // Обновляем ссылку (чтобы clickCount сохранился)
    public void deleteById(int Id) {
        repository.deleteById((long) Id);
    }

    private String generateShortKey(int length){
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            code.append(chars.charAt(index));
        }
        return code.toString();
    }

    // Очистка просроченных ссылок
    @Scheduled(cron = "0 0 3 * * ?") // Каждый день в 3:00 ночи
    @Transactional
    public void cleanupExpiredUrls() {
        int deletedCount = repository.deleteByExpiresAtBefore(LocalDateTime.now());
        log.info("Удалено {} просроченных ссылок", deletedCount);
    }

    public Optional<UrlStatsResponse> getStats(String shortKey) {
        return shortUrlRepository.findByShortKey(shortKey)
                .map(url -> new UrlStatsResponse(
                        url.getShortKey(),
                        url.getOriginalUrl(),
                        url.getClickCount(),
                        url.getClicks().stream()
                                .map(UrlClick::getClickedAt)
                                .max(LocalDateTime::compareTo)
                                .orElse(null)
                ));
    }
    public List<ClickInfo> getClickDetails(String shortKey) {
        return shortUrlRepository.findByShortKey(shortKey)
                .map(url -> url.getClicks().stream()
                        .map(click -> new ClickInfo(
                                click.getClickedAt(),
                                click.getIpAddress(),
                                extractDeviceInfo(click.getUserAgent())
                        ))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private String extractDeviceInfo(String userAgent) {
        // Упрощённый парсинг User-Agent
        if (userAgent == null) return "Unknown";

        if (userAgent.contains("Mobile")) {
            return "Mobile";
        } else if (userAgent.contains("Mac OS")) {
            return "Mac";
        } else if (userAgent.contains("Windows")) {
            return "Windows";
        }
        return "Other";
    }
}
