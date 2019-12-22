package de.elite.musicplayer.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import javax.inject.Inject;

import de.elite.musicplayer.R;
import de.elite.musicplayer.ui.main.MusicPlayer.PlayerState;
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

        Observable<PlayerState> playerStateObservable = musicPlayer.getPlayerState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        playerStateObservable.subscribe(new Observer<PlayerState>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: called");
            }

            @Override
            public void onNext(PlayerState playerState) {
                Log.d(TAG, "onNext: " + playerState);
                refreshPlayerState(playerState);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ", e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: called");
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
        Toast.makeText(getContext(), "Previous pressed", Toast.LENGTH_SHORT).show();
    }

    public void onNextPressed(View view) {
        Toast.makeText(getContext(), "Next pressed", Toast.LENGTH_SHORT).show();
    }

    private void refreshPlayerState(PlayerState playerState) {
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.btn_play_pause);
        if (playerState == PlayerState.PLAY) {
            imageView.setImageResource(R.drawable.ic_pause);
        }
        if (playerState == PlayerState.PAUSE) {
            imageView.setImageResource(R.drawable.ic_play);
        }
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
