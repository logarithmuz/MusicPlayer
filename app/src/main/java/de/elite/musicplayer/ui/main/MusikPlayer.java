package de.elite.musicplayer.ui.main;

public class MusikPlayer {

    private static MusikPlayer instance;

    private PlayerState playerState = PlayerState.PAUSE;

    private MusikPlayer() {
    }

    public static MusikPlayer getInstance() {
        if (MusikPlayer.instance == null) {
            MusikPlayer.instance = new MusikPlayer();
        }
        return MusikPlayer.instance;
    }

    public void playPause() {
        if (this.playerState == PlayerState.PAUSE) {
            this.playerState = PlayerState.PLAY;
            System.out.println("Musikplayer.play()");
        } else if (this.playerState == PlayerState.PLAY) {
            this.playerState = PlayerState.PAUSE;
            System.out.println("Musikplayer.pause()");
        }
    }

    public PlayerState getPlayerState() {
        return this.playerState;
    }

    enum PlayerState {
        PLAY, PAUSE
    }
}
