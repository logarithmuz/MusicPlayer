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
    MusikPlayer musikPlayer = MusikPlayer.getInstance();

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

        ImageView play = (ImageView) v.findViewById(R.id.btn_play_pause);
        play.setOnClickListener(this);
        ImageView next = (ImageView) v.findViewById(R.id.btn_next);
        next.setOnClickListener(this);
        ImageView previous = (ImageView) v.findViewById(R.id.btn_previous);
        previous.setOnClickListener(this);
        return v;
    }

    public void onPlayPausePressed(View view) {
        MusikPlayer.PlayerState playerState = musikPlayer.getPlayerState();
        if (playerState == MusikPlayer.PlayerState.PAUSE)
            Toast.makeText(getContext(),"Play pressed", Toast.LENGTH_SHORT).show();
        if (playerState == MusikPlayer.PlayerState.PLAY)
            Toast.makeText(getContext(), "Pause pressed", Toast.LENGTH_SHORT).show();
        musikPlayer.playPause();
    }

    public void onPreviousPressed(View view) {
        Toast.makeText(getContext(),"Previous pressed", Toast.LENGTH_SHORT).show();
        System.out.println("Previous pressed");
    }

    public void onNextPressed(View view) {
        Toast.makeText(getContext(),"Next pressed", Toast.LENGTH_SHORT).show();
        System.out.println("Next pressed");
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
