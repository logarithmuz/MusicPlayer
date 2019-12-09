package de.elite.musicplayer.ui.main;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

import de.elite.musicplayer.Song;

public class MusicPlayer {

    private static MusicPlayer instance;

    private PlayerState playerState = PlayerState.PAUSE;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    private MusicPlayer() {
    }

    public static MusicPlayer getInstance() {
        if (MusicPlayer.instance == null) {
            MusicPlayer.instance = new MusicPlayer();
        }
        return MusicPlayer.instance;
    }

    public void playPause() {
        if (this.playerState == PlayerState.PAUSE) {
            mediaPlayer.start();
            this.playerState = PlayerState.PLAY;
        } else if (this.playerState == PlayerState.PLAY) {
            mediaPlayer.pause();
            this.playerState = PlayerState.PAUSE;
        }
    }

    public void playSong(Context context, Song song) {
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(context, song.getUri());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerState getPlayerState() {
        return this.playerState;
    }

    enum PlayerState {
        PLAY, PAUSE
    }
}
