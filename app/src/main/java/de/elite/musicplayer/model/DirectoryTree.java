package de.elite.musicplayer.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DirectoryTree {

    private String TAG = "DirectoryTree";

    private String path;
    private String[] pathElements;
    private Map<String, DirectoryTree> subdirectories;
    private List<Song> songs;
    private DirectoryTree parent;

    public DirectoryTree(String path, DirectoryTree parent) {
        this.path = path;
        this.pathElements = path.split("/");
        this.subdirectories = new HashMap<>();
        this.songs = new ArrayList<>();
        this.parent = parent;
    }

    public DirectoryTree getSubdirectorie(String path) {
        return subdirectories.get(path);
    }

    public List<DirectoryTree> getOrderedListOfSubdirectories() {
        List<DirectoryTree> directoryTreeList = subdirectories.values().stream()
                .sorted((o1, o2) -> o1.path.compareTo(o2.path))
                .collect(Collectors.toList());
        return directoryTreeList;
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
        } else {
            this.songs.add(song);
        }
    }

    private void addSubdirectory(String path) {
        this.subdirectories.put(path, new DirectoryTree(path, this));
    }

    public String getName() {
        return pathElements[pathElements.length-1];
    }

    public String getPath() {
        return path;
    }

    public DirectoryTree getParent() {
        return parent;
    }
}
