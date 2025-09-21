package com.example.livenote.controller;

import com.example.livenote.entity.News;
import com.example.livenote.repository.NewsRepository;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    // 外部APIから直接ニュースを取得して返す（DB保存しない）
    @GetMapping("/external")
    public List<News> getExternalNews(@RequestParam String artistName, @RequestParam(defaultValue = "1") int page) {
        // カンマ区切り（jpop,kpop）の場合は分割して合算
        if (artistName.contains(",")) {
            List<News> allNews = new ArrayList<>();
            for (String name : artistName.split(",")) {
                allNews.addAll(newsFetchService.fetchNewsDirect(name.trim(), page));
            }
            return allNews;
        } else {
            return newsFetchService.fetchNewsDirect(artistName, page);
        }
    }

    private final NewsRepository newsRepository;
    private final com.example.livenote.service.NewsFetchService newsFetchService;
    private final com.example.livenote.repository.FavoriteArtistRepository favoriteArtistRepository;

    public NewsController(NewsRepository newsRepository,
            com.example.livenote.service.NewsFetchService newsFetchService,
            com.example.livenote.repository.FavoriteArtistRepository favoriteArtistRepository) {
        this.newsRepository = newsRepository;
        this.newsFetchService = newsFetchService;
        this.favoriteArtistRepository = favoriteArtistRepository;
    }

    // API連携でニュース取得（手動トリガー例）
    @PostMapping("/fetch-external")
    public String fetchExternalNews(@RequestParam String artistName, @RequestParam(required = false) String username) {
        if (artistName == null || artistName.isEmpty()) {
            // ユーザーのお気に入りアーティストのみ取得
            if (username == null || username.isEmpty()) {
                return "Username is required when artistName is empty";
            }
            List<com.example.livenote.entity.FavoriteArtist> favorites = favoriteArtistRepository
                    .findByUser_Username(username);
            for (com.example.livenote.entity.FavoriteArtist fav : favorites) {
                String name = fav.getArtist().getName();
                newsFetchService.fetchNewsApiOrg(name);
            }
            return "OK";
        }
        newsFetchService.fetchNewsApiOrg(artistName);
        return "OK";
    }

    @GetMapping
    public List<News> getNews(@RequestParam(required = false) String artistIds) {
        if (artistIds == null || artistIds.isEmpty()) {
            // 全アーティストの新着ニュース
            return newsRepository.findAll().stream()
                    .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                    .collect(Collectors.toList());
        }
        List<Long> ids = Arrays.stream(artistIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return newsRepository.findByArtistIdInOrderByDateDesc(ids);
    }
}
