package de.elite.musicplayer.model;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import de.elite.musicplayer.model.enums.PlayerState;
import io.reactivex.subjects.PublishSubject;

public class MusicPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private String TAG = "MusicPlayer";
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
        Log.d(TAG, "previousSong()");

        Song song = this.queue.previousSong();
        this.playSong(context, song);
        this.queueSubject.onNext(this.queue);
    }

    public void nextSong(Context context) {
        Log.d(TAG, "nextSong()");

        Song song = this.queue.nextSong();
        this.playSong(context, song);
        this.queueSubject.onNext(this.queue);
    }

    public void createQueueFromSelectionAndPlay(Context context, List<Song> songsInSelection, int clickedSong) {
        Log.d(TAG, "selected song position: " + clickedSong);
        Log.d(TAG, "selected song: " + songsInSelection.get(clickedSong).getArtist() + " - " + songsInSelection.get(clickedSong).getTitle());

        this.queue = new Queue(songsInSelection);
        Song song = this.queue.selectSongAtPosition(clickedSong);
        this.playSong(context, song);
        //this.queueSubject.onNext(this.queue);
        Log.d(TAG, "created Queue and started playing: " + song.getArtist() + "-" + song.getTitle());
    }

    public void playSongAtPositionInQueue(Context context, int clickedSong) {
        Log.d(TAG, "selected song position: " + clickedSong);
        Log.d(TAG, "selected song: " + this.queue.getCurrentSong().getArtist() + " - " + this.queue.getCurrentSong().getTitle());

        Song song = this.queue.selectSongAtPosition(clickedSong);
        this.playSong(context, song);
        this.queueSubject.onNext(this.queue);
    }

    private void playSong(Context context, Song song) {
        if (context == null) {
            context = this.lastCotext;
        } else {
            this.lastCotext = context;
        }
        try {
            this.playerState = PlayerState.STOP;
            playerStateSubject.onNext(this.playerState);
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

    public void seekToPosiontInSong(int millis) {
        mediaPlayer.seekTo(millis);
    }

    public int getCurrentPositionInSong() {
        if (playerState == PlayerState.STOP) {
            // do not ask for current position if mediaplayer is stopped, that would cause an error
            // that triggers onComplete and therefore start the next song
            return -1;
        }
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
        Log.d(TAG, "onPrepared()");

        mp.start();
        mp.setOnCompletionListener(this);
        this.playerState = PlayerState.PLAY;
        playerStateSubject.onNext(this.playerState);
        Song song = this.queue.getCurrentSong();
        this.currentSongSubject.onNext(song);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion()");

        this.playerState = PlayerState.STOP;
        playerStateSubject.onNext(this.playerState);
        mp.stop();
        mp.reset();
        this.nextSong(null);
    }
}
