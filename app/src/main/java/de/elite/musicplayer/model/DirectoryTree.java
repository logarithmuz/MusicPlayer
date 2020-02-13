package de.elite.musicplayer.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectoryTree {

    private String TAG = "DirectoryTree";

    private String path;
    private String[] pathElements;
    private Map<String, DirectoryTree> subdirectories;
    private List<Song> songs;

    public DirectoryTree(String path) {
        this.path = path;
        this.pathElements = path.split("/");
        this.subdirectories = new HashMap<>();
        this.songs = new ArrayList<>();
    }

    public Map<String, DirectoryTree> getSubdirectories() {
        return subdirectories;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        String songPath = song.getPath();
        String[] songPathElements = songPath.split("/");
        for (int i = 0; i < pathElements.length; i++) {
            if (!pathElements[i].equals(songPathElements[i])) {
                Log.d(TAG, "ignored song: " + songPath);
                return; // ignore songs outside of the default music directory
            }
        }
        final int SONG_NAME = 1;
        if (songPathElements.length - SONG_NAME > pathElements.length) {
            String subdirectoryPath = this.path + "/" + songPathElements[pathElements.length];
            if (!this.subdirectories.containsKey(subdirectoryPath)) {
                Log.d(TAG, "create subdirectory '" + subdirectoryPath + "' for '" + songPath + "'");
                addSubdirectory(subdirectoryPath);
            }
            this.subdirectories.get(subdirectoryPath).addSong(song);
        }
    }

    private void addSubdirectory(String path) {
        this.subdirectories.put(path, new DirectoryTree(path));
    }
}
