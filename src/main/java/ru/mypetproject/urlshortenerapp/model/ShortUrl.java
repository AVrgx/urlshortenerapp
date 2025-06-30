package ru.mypetproject.urlshortenerapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(nullable = false, unique = true)
    public String originalUrl;
    @Column(nullable = false)
    public String shortKey;
    @Column(nullable = false)
    LocalDateTime createdAt;

    LocalDateTime expiresAt;

    @Column(nullable = false)
    private int clickCount = 0;

    @OneToMany(mappedBy = "shortUrl", cascade = CascadeType.ALL)
    private List<UrlClick> clicks = new ArrayList<>();

}
