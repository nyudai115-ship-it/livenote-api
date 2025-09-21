package com.example.livenote.repository;

import com.example.livenote.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByArtistIdInOrderByDateDesc(List<Long> artistIds);

    List<News> findByArtistIdOrderByDateDesc(Long artistId);
}
