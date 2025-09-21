package com.example.livenote.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.livenote.entity.Song;
import com.example.livenote.repository.SongRepository;

@RestController
@RequestMapping("/api/v1/songs")
public class SongController {

    private final SongRepository songRepository;

    public SongController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @GetMapping("")
    public Iterable<Song> findAllSongs() {
        return songRepository.findAll();
    }

    @PostMapping("")
    public Song createSong(@RequestBody Song song) {
        return songRepository.save(song);
    }
}
