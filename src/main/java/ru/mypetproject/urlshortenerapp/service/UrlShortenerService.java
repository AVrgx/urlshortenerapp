package ru.mypetproject.urlshortenerapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mypetproject.urlshortenerapp.model.ShortUrl;
import ru.mypetproject.urlshortenerapp.repository.ShortUrlRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {
    private final ShortUrlRepository repository;
    private static final int SHORT_KEY_LENGTH = 6;

    public String ShortenUrl(String originalUrl, Integer ttlDays) {
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

    public Optional<String> getOriginalUrl(String shortKey) {
        return repository.findByShortKey(shortKey)
                .filter(url ->url.getExpiresAt() == null
                || LocalDateTime.now().isBefore(url.getExpiresAt()))
                .map(ShortUrl::getOriginalUrl);
    }

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
}
