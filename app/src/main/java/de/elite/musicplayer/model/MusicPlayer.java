package de.elite.musicplayer.model;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.List;

import de.elite.musicplayer.model.enums.PlayerState;
import io.reactivex.subjects.PublishSubject;

public class MusicPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private static MusicPlayer instance;

    private PlayerState playerState = PlayerState.PAUSE;
    private PublishSubject<PlayerState> playerStateSubject = PublishSubject.create();

    private Queue queue;
    private PublishSubject<Queue> queueSubject = PublishSubject.create();
    private PublishSubject<Song> currentSongSubject = PublishSubject.create();

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Context lastCotext;

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

    public void previousSong(Context context) {
        Song song = this.queue.previousSong();
        this.playSong(context, song);
        this.currentSongSubject.onNext(song);
        this.queueSubject.onNext(this.queue);
    }

    public void nextSong(Context context) {
        Song song = this.queue.nextSong();
        this.playSong(context, song);
        this.currentSongSubject.onNext(song);
        this.queueSubject.onNext(this.queue);
    }

    public void createQueueFromSelectionAndPlay(Context context, List<Song> songsInSelection, int clickedSong){
        this.queue = new Queue(songsInSelection);
        Song song = this.queue.playSongAtPosition(clickedSong);
        this.playSong(context, song);
        this.queueSubject.onNext(this.queue);
        this.currentSongSubject.onNext(song);
    }

    public void playSongAtPositionInQueue(Context context, int position) {
        Song song = this.queue.playSongAtPosition(position);
        this.playSong(context, song);
        this.queueSubject.onNext(this.queue);
        this.currentSongSubject.onNext(song);
    }

    public void playSong(Context context, Song song) {
        if (context == null){
            context = this.lastCotext;
        } else {
            this.lastCotext = context;
        }
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(context, song.getUri());
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync(); // prepare async to not block main thread
            this.currentSongSubject.onNext(song);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seekToPosiontInSong(int millis) {
        mediaPlayer.seekTo(millis);
    }

    public int getCurrentPositionInSong() {
        return mediaPlayer.getCurrentPosition();
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

    public List<Song> getQueueSongList() {
        return this.queue.getSongList();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        mp.setOnCompletionListener(this);
        this.playerState = PlayerState.PLAY;
        playerStateSubject.onNext(this.playerState);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.stop();
        mp.reset();
        this.nextSong(null);
    }
}
