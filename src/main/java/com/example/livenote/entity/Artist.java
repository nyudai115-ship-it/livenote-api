package com.example.livenote.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "artists")
@Data // @Getter, @Setter, @ToStringなどを自動生成
@NoArgsConstructor // 引数のないコンストラクタを自動生成
public class Artist {
    @javax.persistence.Column(name = "created_at")
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private java.util.Date createdAt;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @javax.persistence.Column(name = "spotify_id")
    private String spotifyId;
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Album> albums;
}
