package de.elite.musicplayer.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import javax.inject.Inject;

import de.elite.musicplayer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayerFragment extends Fragment implements View.OnClickListener {

    @Inject
    MusicPlayer musicPlayer = MusicPlayer.getInstance();

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
        MusicPlayer.PlayerState playerState = musicPlayer.getPlayerState();
        ImageView imageView = (ImageView) view;
        if (playerState == MusicPlayer.PlayerState.PAUSE) {
            imageView.setImageResource(R.drawable.ic_pause);
        }
        if (playerState == MusicPlayer.PlayerState.PLAY) {
            imageView.setImageResource(R.drawable.ic_play);
        }
        musicPlayer.playPause();
    }

    public void onPreviousPressed(View view) {
        Toast.makeText(getContext(), "Previous pressed", Toast.LENGTH_SHORT).show();
    }

    public void onNextPressed(View view) {
        Toast.makeText(getContext(), "Next pressed", Toast.LENGTH_SHORT).show();
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
