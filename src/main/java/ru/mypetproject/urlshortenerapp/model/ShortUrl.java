package ru.mypetproject.urlshortenerapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

}
