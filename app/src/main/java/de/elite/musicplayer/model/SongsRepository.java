package de.elite.musicplayer.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import de.elite.musicplayer.controller.MusicPlayerApplication;


public class SongsRepository {

    private static SongsRepository instance;

    private SongsRepository() {
    }

    public static SongsRepository getInstance() {
        if (SongsRepository.instance == null) {
            SongsRepository.instance = new SongsRepository();
        }
        return SongsRepository.instance;
    }

    public List<Song> getAllSongs() {
        List<Song> songList = new ArrayList();
        Context context = MusicPlayerApplication.get();
        ContentResolver contentResolver = context.getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(musicUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songAlbum = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int songAlbumID = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            do {
                String title = songCursor.getString(songTitle);
                String artist = songCursor.getString(songArtist);
                String album = songCursor.getString(songAlbum);
                int albumID = songCursor.getInt(songAlbumID);
                int duration = songCursor.getInt(songDuration);
                String path = songCursor.getString(songPath);
                Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        songCursor.getInt(songCursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID)));

                songList.add(new Song(songId, title, artist, album, albumID, duration, path, songUri));
            } while (songCursor.moveToNext());
        }
        return songList;
    }
}
