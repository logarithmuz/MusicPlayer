package de.elite.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;


public class SongsRepository {

    private static SongsRepository instance;

    private SongsRepository () {}

    public static SongsRepository getInstance () {
        if (SongsRepository.instance == null) {
            SongsRepository.instance = new SongsRepository();
        }
        return SongsRepository.instance;
    }

    public List<Song> getAllSongs() {
        System.out.println("getAllSongs()");
        List<Song> songList = new ArrayList();
        Context context = MusicPlayerApplication.get();
        ContentResolver contentResolver = context.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(musicUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            do {
                String title = songCursor.getString(songTitle);
                String artist = songCursor.getString(songArtist);
                Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        songCursor.getInt(songCursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID)));
                songList.add(new Song(songId, title, artist, songUri));
            } while (songCursor.moveToNext());
        }
        return songList;
    }
}
