package ru.mypetproject.urlshortenerapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mypetproject.urlshortenerapp.dto.ShortenRequest;
import ru.mypetproject.urlshortenerapp.service.UrlShortenerService;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final UrlShortenerService service;

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("shortenRequest", new ShortenRequest());
        return "index"; // Шаблон index.html
    }

    @PostMapping("/shorten")
    public String shortenUrl(
            HttpServletRequest request,
            @RequestParam("url") String url,
            @RequestParam(value = "ttlDays", defaultValue = "30") int ttlDays,
            Model model) {

        try {
            String shortKey = service.shortenUrl(url, ttlDays);

            String baseUrl = request.getScheme() + "://" + request.getServerName() +
                             ":" + request.getServerPort();
            String shortUrl = baseUrl + "/api/" + shortKey;

            model.addAttribute("shortUrl", shortUrl);
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/stats")
    public String showStats(Model model) {
        model.addAttribute("urls", service.getAllUrls());
        return "stats"; // Шаблон stats.html
    }
}