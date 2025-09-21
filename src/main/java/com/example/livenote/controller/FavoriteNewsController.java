package com.example.livenote.controller;

import com.example.livenote.entity.FavoriteNews;
import com.example.livenote.repository.FavoriteNewsRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorite-news")
public class FavoriteNewsController {
    // お気に入り追加・削除（トグル）API
    @PostMapping("/toggle")
    public org.springframework.http.ResponseEntity<?> toggleFavoriteNews(@RequestBody FavoriteNews news) {
        java.util.List<FavoriteNews> existing = favoriteNewsRepository.findByUserAndTitleAndUrlAndArtistName(
                news.getUser(), news.getTitle(), news.getUrl(), news.getArtistName());
        if (existing != null && !existing.isEmpty()) {
            // 既にお気に入り→削除
            for (FavoriteNews fav : existing) {
                favoriteNewsRepository.delete(fav);
            }
            return org.springframework.http.ResponseEntity.ok()
                    .body(java.util.Collections.singletonMap("result", "removed"));
        } else {
            // 未登録→追加
            favoriteNewsRepository.save(news);
            return org.springframework.http.ResponseEntity.ok()
                    .body(java.util.Collections.singletonMap("result", "added"));
        }
    }

    // お気に入り記事一覧取得API
    @GetMapping("/list")
    public java.util.List<FavoriteNews> getFavoriteNewsList(@RequestParam("user") String user) {
        return favoriteNewsRepository.findByUser(user);
    }

    private final FavoriteNewsRepository favoriteNewsRepository;

    public FavoriteNewsController(FavoriteNewsRepository favoriteNewsRepository) {
        this.favoriteNewsRepository = favoriteNewsRepository;
    }

    @PostMapping("/add")
    public org.springframework.http.ResponseEntity<?> addFavoriteNews(@RequestBody FavoriteNews news) {
        try {
            favoriteNewsRepository.save(news);
            return org.springframework.http.ResponseEntity.ok()
                    .body(java.util.Collections.singletonMap("result", "OK"));
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.status(500)
                    .body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }
}
