package com.example.livenote.repository;

import com.example.livenote.entity.FavoriteArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist, Long> {
    List<FavoriteArtist> findByUser_Username(String username);

    void deleteByUser_UsernameAndArtist_Id(String username, Long artistId);
}
