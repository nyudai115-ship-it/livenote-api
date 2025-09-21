package com.example.livenote.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.livenote.entity.Album;
import com.example.livenote.repository.AlbumRepository;

@RestController
@RequestMapping("/api/v1")
public class AlbumController {

    private final AlbumRepository albumRepository;

    public AlbumController(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @GetMapping("/albums")
    public Iterable<Album> findAllAlbums() {
        return albumRepository.findAll();
    }

    @PostMapping("/albums")
    public Album createAlbum(@RequestBody Album album) {
        return albumRepository.save(album);
    }

    @PutMapping("/albums/{id}")
    public Album updateAlbum(@PathVariable Long id, @RequestBody Album albumDetails) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album not found with id " + id));

        album.setName(albumDetails.getName());
        album.setSpotifyId(albumDetails.getSpotifyId());

        return albumRepository.save(album);
    }

    @DeleteMapping("/albums/{id}")
    public ResponseEntity<?> deleteAlbum(@PathVariable Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album not found with id " + id));

        albumRepository.delete(album);

        return ResponseEntity.ok().build();
    }
}
