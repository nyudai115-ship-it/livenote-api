package com.example.livenote.controller;

import com.example.livenote.entity.FavoriteArtist;
import com.example.livenote.entity.Artist;
import com.example.livenote.entity.User;
import com.example.livenote.repository.FavoriteArtistRepository;
import com.example.livenote.repository.ArtistRepository;
import com.example.livenote.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/favorite-artists")
public class FavoriteArtistController {
    // お気に入りアーティスト一覧取得API（GET /list?user=...）
    @GetMapping("/list")
    public List<Artist> getFavoriteList(@RequestParam("user") String username) {
        List<FavoriteArtist> favorites = favoriteArtistRepository.findByUser_Username(username);
        return favorites.stream()
                .filter(a -> a != null)
                .map(FavoriteArtist::getArtist)
                .collect(Collectors.toList());
    }

    private final FavoriteArtistRepository favoriteArtistRepository;
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;

    public FavoriteArtistController(FavoriteArtistRepository favoriteArtistRepository,
            ArtistRepository artistRepository,
            UserRepository userRepository) {
        this.favoriteArtistRepository = favoriteArtistRepository;
        this.artistRepository = artistRepository;
        this.userRepository = userRepository;
    }

    // DTO for add/remove
    public static class FavoriteArtistDTO {
        private String username;
        private Long artistId;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Long getArtistId() {
            return artistId;
        }

        public void setArtistId(Long artistId) {
            this.artistId = artistId;
        }
    }

    @PostMapping("/add")
    public FavoriteArtist addFavorite(@RequestBody FavoriteArtistDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername()).orElse(null);
        Artist artist = artistRepository.findById(dto.getArtistId()).orElse(null);
        if (user == null || artist == null)
            throw new IllegalArgumentException("ユーザーまたはアーティストが存在しません");
        FavoriteArtist fav = new FavoriteArtist();
        fav.setUser(user);
        fav.setArtist(artist);
        return favoriteArtistRepository.save(fav);
    }

    @PostMapping("/remove")
    public void removeFavorite(@RequestBody FavoriteArtistDTO dto) {
        favoriteArtistRepository.deleteByUser_UsernameAndArtist_Id(dto.getUsername(), dto.getArtistId());
    }
}
