package com.example.livenote.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite_artists")
@Data
@NoArgsConstructor
public class FavoriteArtist {
    @javax.persistence.Column(name = "created_at")
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private java.util.Date createdAt;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    public Long getArtistId() {
        return artist != null ? artist.getId() : null;
    }

    public void setUsername(String username) {
        if (user == null)
            user = new User();
        user.setUsername(username);
    }

    public void setArtistId(Long artistId) {
        if (artist == null)
            artist = new Artist();
        artist.setId(artistId);
    }
}