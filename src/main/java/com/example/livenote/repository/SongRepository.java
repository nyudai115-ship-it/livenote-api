package com.example.livenote.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.livenote.entity.Song;

public interface SongRepository extends CrudRepository<Song, Long> {
}