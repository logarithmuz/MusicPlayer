package de.elite.musicplayer;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.elite.musicplayer.ui.main.QueuePosition;


public class Queue {

    private String TAG = "Queue";
    private List<Song> songList;
    private int currentSong;

    public Queue() {
        this.songList = new ArrayList<>();
        this.currentSong = 0;
    }

    public List<Song> getSongList() {
        return this.songList;
    }

    public Song nextSong() {
        if (currentSong < songList.size() - 1) {
            currentSong++;
        } else {
            // if last song finished playing, start with first song again
            currentSong = 0;
        }
        return songList.get(currentSong);
    }

    public Song previousSong() {
        if (currentSong > 0) {
            currentSong--;
        }
        return songList.get(currentSong);
    }

    public Song playSongAtPosition(int position) {
        if (position < 0 || position >= songList.size()) {
            Log.d(TAG, "Invalid position in Queue selected: " + position +
                    ". Available Positions are between 0 and " + (songList.size() - 1));
            return null;
        }
        currentSong = position;
        return songList.get(currentSong);
    }

    public void addSongToQueue(QueuePosition position, Song songToAdd) {
        switch (position) {
            case BEFORE_CURRENT_SONG:
                addSongAtPosition(currentSong, songToAdd);
                currentSong++;
                break;
            case AFTER_CURRENT_SONG:
                addSongAtPosition(currentSong + 1, songToAdd);
                break;
            case FIRST:
                addSongAtPosition(0, songToAdd);
                break;
            case LAST:
                this.songList.add(songToAdd);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
    }

    private void addSongAtPosition(int position, Song songToAdd) {
        if (position < 0) {
            this.songList.add(0, songToAdd);
        } else if (position >= this.songList.size()) {
            this.songList.add(songToAdd);
        } else {
            this.songList.add(position, songToAdd);
        }
    }

    public Song getCurrentSong() {
        if (this.songList.size() > 0) {
            return this.songList.get(currentSong);
        }
        Log.d(TAG, "Cannot return current song, queue is empty");
        return null;
    }
}
