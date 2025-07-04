package ru.mypetproject.urlshortenerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mypetproject.urlshortenerapp.model.ShortUrl;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortKey(String shortKey);
    Optional<ShortUrl> findByOriginalUrl(String originalUrl);
    boolean existsByShortKey(String shortKey);

    @Modifying // Позволяет изменять данные
    @Query("DELETE FROM ShortUrl s WHERE s.expiresAt < :currentTime")
    int deleteByExpiresAtBefore(@Param("currentTime") LocalDateTime currentTime);
}
