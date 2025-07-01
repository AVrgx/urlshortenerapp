package ru.mypetproject.urlshortenerapp.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mypetproject.urlshortenerapp.dto.ShortenRequest;
import ru.mypetproject.urlshortenerapp.service.UrlShortenerService;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class WebController {

    private final UrlShortenerService service;

    // Главная страница с формой
    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("shortenRequest", new ShortenRequest());
        return "index"; // Имя шаблона без .html
    }

    // Обработка формы
    @PostMapping("/shorten")
    public String handleShortenRequest(
            @ModelAttribute ShortenRequest shortenRequest,
            Model model
    ) {
        String shortKey = service.shortenUrl(
                shortenRequest.getUrl(),
                shortenRequest.getTtlDays()
        );

        model.addAttribute("originalUrl", shortenRequest.getUrl());
        model.addAttribute("shortUrl", "http://localhost:8080/" + shortKey);
        return "result";
    }
    // Страница со списком всех ссылок
    @GetMapping("/urls")
    public String showAllUrls(Model model) {
        model.addAttribute("urls", service.getAllUrls());
        return "urls";
    }
}
