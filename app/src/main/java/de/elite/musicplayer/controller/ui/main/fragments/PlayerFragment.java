package de.elite.musicplayer.controller.ui.main.fragments;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import de.elite.musicplayer.controller.Constant;
import de.elite.musicplayer.R;
import de.elite.musicplayer.controller.ui.helper.threads.UpdateSeekBarThread;
import de.elite.musicplayer.model.Song;
import de.elite.musicplayer.model.MusicPlayer;
import de.elite.musicplayer.model.enums.PlayerState;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener {

    @Inject
    MusicPlayer musicPlayer = MusicPlayer.getInstance();
    private String TAG = "PlayerFragment";
    private PlayerState playerState;
    private UpdateSeekBarThread updateSeekBarThread;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlayerFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerFragment newInstance() {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        Observable<PlayerState> playerStateObservable = musicPlayer.getPlayerStateSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        playerStateObservable.subscribe(new Observer<PlayerState>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "playerStateObservable/onSubscribe: called");
            }

            @Override
            public void onNext(PlayerState playerState) {
                Log.d(TAG, "playerStateObservable/onNext: " + playerState);
                refreshPlayerState(playerState);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "playerStateObservable/onError: ", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "playerStateObservable/onComplete: called");
            }
        });

        Observable<Song> currentSongObservable = musicPlayer.getCurrentSongSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        currentSongObservable.subscribe(new Observer<Song>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "currentSongObservable/onSubscribe: called");
            }

            @Override
            public void onNext(Song song) {
                Log.d(TAG, "currentSongObservable/onNext: " + song.toString());
                refreshSongInfo(song);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "currentSongObservable/onError: ", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "currentSongObservable/onComplete: called");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);

        ImageView play = v.findViewById(R.id.btn_play_pause);
        play.setOnClickListener(this);
        ImageView next = v.findViewById(R.id.btn_next);
        next.setOnClickListener(this);
        ImageView previous = v.findViewById(R.id.btn_previous);
        previous.setOnClickListener(this);
        return v;
    }

    public void onPlayPausePressed(View view) {
        musicPlayer.playPause();
    }

    public void onPreviousPressed(View view) {
        musicPlayer.previousSong(getContext());
    }

    public void onNextPressed(View view) {
        musicPlayer.nextSong(getContext());
    }

    private void refreshPlayerState(PlayerState playerState) {
        this.playerState = playerState;
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.btn_play_pause);
        if (playerState == PlayerState.PLAY) {
            imageView.setImageResource(R.drawable.ic_pause);
        }
        if (playerState == PlayerState.PAUSE) {
            imageView.setImageResource(R.drawable.ic_play);
        }
        if (playerState == PlayerState.STOP) {
            if (this.updateSeekBarThread != null) {
                this.updateSeekBarThread.stopThread();
            }
        }
    }

    private void refreshSongInfo(final Song song) {
        Log.d(TAG, "refreshSongInfo() called");
        try {
            ImageView albumCover = getActivity().findViewById(R.id.album_cover);
            TextView tfTitle = getActivity().findViewById(R.id.tf_title);
            TextView tfArtist = getActivity().findViewById(R.id.tf_artist);
            TextView tfAlbum = getActivity().findViewById(R.id.tf_album);
            TextView tfSeekBarPosition = getActivity().findViewById(R.id.tf_seekbar_position);
            TextView tfSeekBarDuration = getActivity().findViewById(R.id.tf_seekbar_duration);
            SeekBar seekBar = getActivity().findViewById(R.id.seekBar);

            Uri uri = ContentUris.withAppendedId(Constant.sArtworkUri, song.getAlbumID());
            ContentResolver res = getContext().getContentResolver();
            InputStream in = res.openInputStream(uri);
            Bitmap artwork = BitmapFactory.decodeStream(in);

            albumCover.setImageBitmap(artwork);
            tfTitle.setText(song.getTitle());
            tfArtist.setText(song.getArtist());
            tfAlbum.setText(song.getAlbum());
            tfSeekBarPosition.setText("0:00");
            tfSeekBarDuration.setText(formatTime(song.getDuration()));
            seekBar.setMax(song.getDuration());

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser)
                        musicPlayer.seekToPosiontInSong(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            updateSeekBarThread = new UpdateSeekBarThread(musicPlayer, song, this);
            updateSeekBarThread.start();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void refreshSeekBarSeconds(final int millis) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                TextView tf = getActivity().findViewById(R.id.tf_seekbar_position);
                if (tf != null)
                    tf.setText(formatTime(millis));
            }
        });
    }

    public void refreshSeekBar(int millis) {
        SeekBar seekBar = getActivity().findViewById(R.id.seekBar);
        if (seekBar != null)
            seekBar.setProgress(millis);
    }

    private String formatTime(int millis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
        String time = String.format("%d:%02d", minutes, seconds);
        return time;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play_pause:
                onPlayPausePressed(v);
                break;
            case R.id.btn_next:
                onNextPressed(v);
                break;
            case R.id.btn_previous:
                onPreviousPressed(v);
                break;
            default:
                throw new UnsupportedOperationException("No onClick action defined for " + v.getId());
        }
    }
}
