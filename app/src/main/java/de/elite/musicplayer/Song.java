package de.elite.musicplayer;

import android.net.Uri;

public class Song {

    private int songId;
    private String title;
    private String artist;
    private Uri songUri;


    public Song(int songId, String title, String artist, Uri songUri) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.songUri = songUri;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}
