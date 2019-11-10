package de.elite.musicplayer;

import android.app.Application;

public class MusicPlayerApplication extends Application {
    private static MusicPlayerApplication instance;
    public static MusicPlayerApplication get() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
