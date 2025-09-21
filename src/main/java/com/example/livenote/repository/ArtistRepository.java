package com.example.livenote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.livenote.entity.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}