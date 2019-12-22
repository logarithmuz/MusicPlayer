package de.elite.musicplayer;

import android.net.Uri;

import androidx.annotation.NonNull;

public class Song {

    private int songId;
    private String title;
    private String artist;
    private String album;
    private int albumID;
    private int duration;
    private String path;
    private Uri songUri;


    public Song(int songId, String title, String artist, String album, int albumID, int duration, String path, Uri songUri) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumID = albumID;
        this.duration = duration;
        this.path = path;
        this.songUri = songUri;
    }

    @NonNull
    @Override
    public String toString() {
        return "title: " + title + ", artist: " + artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public int getAlbumID() {
        return albumID;
    }

    public int getDuration() {
        return duration;
    }

    public Uri getUri() {
        return songUri;
    }
}
