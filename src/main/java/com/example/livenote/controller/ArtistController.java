package com.example.livenote.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.livenote.entity.Artist;
import com.example.livenote.repository.ArtistRepository;
import com.example.livenote.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1/artists")
public class ArtistController {

    private final ArtistRepository artistRepository;

    // コンストラクタインジェクション
    public ArtistController(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @GetMapping
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id) {
        return artistRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Artist createArtist(@RequestBody Artist artist) {
        return artistRepository.save(artist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable Long id, @RequestBody Artist artistDetails) {
        return artistRepository.findById(id)
                .map(artist -> {
                    artist.setName(artistDetails.getName());
                    artist.setSpotifyId(artistDetails.getSpotifyId());
                    Artist updated = artistRepository.save(artist);
                    return ResponseEntity.ok(updated);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArtist(@PathVariable Long id) {
        return artistRepository.findById(id)
                .map(artist -> {
                    artistRepository.delete(artist);
                    return ResponseEntity.ok().build();
                })
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id " + id));
    }
}