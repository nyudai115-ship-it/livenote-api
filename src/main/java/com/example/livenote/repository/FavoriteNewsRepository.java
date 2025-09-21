package com.example.livenote.repository;

import com.example.livenote.entity.FavoriteNews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteNewsRepository extends JpaRepository<FavoriteNews, Long> {
    java.util.List<FavoriteNews> findByUserAndTitleAndUrlAndArtistName(String user, String title, String url,
            String artistName);

    java.util.List<FavoriteNews> findByUser(String user);
    // 必要に応じてカスタムメソッド追加可能
}
