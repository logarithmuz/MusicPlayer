package de.elite.musicplayer.controller.ui.helper.threads;

import android.util.Log;

import de.elite.musicplayer.controller.ui.main.fragments.PlayerFragment;
import de.elite.musicplayer.model.MusicPlayer;
import de.elite.musicplayer.model.Song;

public class UpdateSeekBarThread extends Thread {
    private String TAG = "UpdateSeekbarThread";

    private MusicPlayer musicPlayer;
    private Song song;
    private PlayerFragment playerFragment;
    private boolean stop;

    public UpdateSeekBarThread(MusicPlayer musicPlayer, Song song, PlayerFragment playerFragment) {
        Log.d(TAG, "instanciate Thread");
        this.musicPlayer = musicPlayer;
        this.song = song;
        this.playerFragment = playerFragment;
    }

    @Override
    public void run() {
        while (true) {
            if (stop) {
                break;
            }
            int currendPositionInSong = musicPlayer.getCurrentPositionInSong();
            if (currendPositionInSong != -1) {
                if (currendPositionInSong >= song.getDuration()) {
                    break;
                } else {
                    playerFragment.refreshSeekBar(currendPositionInSong);
                    playerFragment.refreshSeekBarSeconds(currendPositionInSong);
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "terminate Thread");
    }

    public void stopThread(){
        this.stop = true;
    }
}
