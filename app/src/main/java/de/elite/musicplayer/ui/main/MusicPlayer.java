package de.elite.musicplayer.ui.main;

public class MusicPlayer {

    private static MusicPlayer instance;

    private PlayerState playerState = PlayerState.PAUSE;

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
            this.playerState = PlayerState.PLAY;
        } else if (this.playerState == PlayerState.PLAY) {
            this.playerState = PlayerState.PAUSE;
        }
    }

    public PlayerState getPlayerState() {
        return this.playerState;
    }

    enum PlayerState {
        PLAY, PAUSE
    }
}
