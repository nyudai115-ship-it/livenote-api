package com.example.livenote.service;

import com.example.livenote.entity.Artist;
import com.example.livenote.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Iterable<Artist> findAllArtists() {
        return artistRepository.findAll();
    }

    public Optional<Artist> findArtistById(Long id) {
        return artistRepository.findById(id);
    }

    public Artist saveArtist(Artist artist) {
        return artistRepository.save(artist);
    }

    public void deleteArtistById(Long id) {
        artistRepository.deleteById(id);
    }
}