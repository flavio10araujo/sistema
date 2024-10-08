package com.polifono.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity
@Table(name = "t022_playervideo")
public class Playervideo {
    @Id
    @Column(name = "c022_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "c001_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "c009_id")
    private Content content;

    @Column(name = "c022_dt_inc")
    private Date dtInc;

    @Column(name = "c022_active")
    private boolean active;

    @Column(name = "c022_url")
    private String url;

    public String getUrlFormatted() {
        if (this.url == null || this.url.isEmpty()) {
            return "";
        }

        String videoId = extractYoutubeVideoIdFromUrl();
        if (videoId.isEmpty()) {
            return "";
        }

        return "https://www.youtube.com/embed/" + videoId;
    }

    private String extractYoutubeVideoIdFromUrl() {
        String videoId = "";

        if (this.url.startsWith("https://www.youtube.com/watch?v=")) {
            videoId = this.url.substring(this.url.indexOf("?v=") + 3);
        } else if (this.url.startsWith("https://youtu.be/")) {
            videoId = this.url.substring(this.url.indexOf(".be/") + 4);
        } else if (this.url.startsWith("https://m.youtube.com/watch?v=")) {
            videoId = this.url.substring(this.url.indexOf("?v=") + 3);
        }

        if (videoId.contains("&")) {
            videoId = videoId.substring(0, videoId.indexOf("&"));
        }

        return videoId;
    }
}
