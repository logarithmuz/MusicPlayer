package de.elite.musicplayer;

import android.net.Uri;

public class Song {

    private int songId;
    private String title;
    private String artist;
    private String path;
    private Uri songUri;


    public Song(int songId, String title, String artist, String path, Uri songUri) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.path = path;
        this.songUri = songUri;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}
