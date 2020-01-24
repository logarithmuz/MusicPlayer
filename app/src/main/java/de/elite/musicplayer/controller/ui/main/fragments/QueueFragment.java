package de.elite.musicplayer.controller.ui.main.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import de.elite.musicplayer.model.Queue;
import de.elite.musicplayer.controller.ui.helper.adapter.QueueFragmentRecyclerViewAdapter;
import de.elite.musicplayer.R;
import de.elite.musicplayer.model.Song;
import de.elite.musicplayer.model.MusicPlayer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QueueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueueFragment extends Fragment {

    @Inject
    MusicPlayer musicPlayer = MusicPlayer.getInstance();
    private String TAG = "QueueFragment";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QueueFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QueueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QueueFragment newInstance() {
        QueueFragment fragment = new QueueFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        Observable<Queue> queueObservable = musicPlayer.getQueueSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        queueObservable.subscribe(new Observer<Queue>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: called");
            }

            @Override
            public void onNext(Queue queue) {
                Log.d(TAG, "onNext: " + queue.getCurrentSong());
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_queue, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            final List<Song> queueSongList = this.musicPlayer.getQueueSongList();

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new QueueFragmentRecyclerViewAdapter(queueSongList, new OnListFragmentInteractionListener() {
                @Override
                public void onFragmentInteraction(Song song) {
                    musicPlayer.playSongAtPositionInQueue(getContext(), queueSongList.indexOf(song));
                }
            }));
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onFragmentInteraction(Song item);
    }
}
