package ru.mypetproject.urlshortenerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mypetproject.urlshortenerapp.model.UrlClick;

import java.util.List;

public interface UrlClickRepository extends JpaRepository<UrlClick, Long> {
    // Найти все клики по ID короткой ссылки
    List<UrlClick> findByShortUrlId(Long shortUrlId);
}
