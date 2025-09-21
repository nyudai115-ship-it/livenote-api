package com.example.livenote.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.livenote.entity.Album;

public interface AlbumRepository extends CrudRepository<Album, Long> {
}