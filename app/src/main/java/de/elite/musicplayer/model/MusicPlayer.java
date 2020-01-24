package de.elite.musicplayer.model;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.List;

import de.elite.musicplayer.model.enums.PlayerState;
import de.elite.musicplayer.model.enums.QueuePosition;
import io.reactivex.subjects.PublishSubject;

public class MusicPlayer implements MediaPlayer.OnPreparedListener {

    private static MusicPlayer instance;

    private PlayerState playerState = PlayerState.PAUSE;
    private PublishSubject<PlayerState> playerStateSubject = PublishSubject.create();

    private Queue queue;
    private PublishSubject<Queue> queueSubject = PublishSubject.create();
    private PublishSubject<Song> currentSongSubject = PublishSubject.create();

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private MusicPlayer() {
        this.playerStateSubject.onNext(playerState);
        this.queue = new Queue();
        this.queueSubject.onNext(queue);
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
        playerStateSubject.onNext(this.playerState);
    }

    public void playSong(Context context, Song song) {
        try {
            this.queue.addSongToQueue(QueuePosition.AFTER_CURRENT_SONG, song);
            this.queue.nextSong();
            this.currentSongSubject.onNext(song);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(context, song.getUri());
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync(); // prepare async to not block main thread
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PublishSubject<PlayerState> getPlayerStateSubject() {
        return playerStateSubject;
    }

    public PublishSubject<Song> getCurrentSongSubject() {
        return currentSongSubject;
    }

    public PublishSubject<Queue> getQueueSubject() {
        return queueSubject;
    }

    public void seekToPosiontInSong(int millis) {
        mediaPlayer.seekTo(millis);
    }

    public int getCurrentPositionInSong() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        this.playerState = PlayerState.PLAY;
        playerStateSubject.onNext(this.playerState);
    }

    public List<Song> getQueueSongList() {
        return this.queue.getSongList();
    }
}
