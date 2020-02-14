package de.elite.musicplayer.controller.ui.main.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import javax.inject.Inject;

import de.elite.musicplayer.controller.ui.helper.adapter.FolderFragmentRecyclerViewAdapter;
import de.elite.musicplayer.R;
import de.elite.musicplayer.model.DirectoryTree;
import de.elite.musicplayer.model.Song;
import de.elite.musicplayer.model.SongsRepository;
import de.elite.musicplayer.model.MusicPlayer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FolderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FolderFragment extends Fragment implements View.OnClickListener {

    private String TAG = "FolderFragment";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount;
    private DirectoryTree currentDirectoryTree;

    @Inject
    private SongsRepository songsRepository = SongsRepository.getInstance();

    @Inject
    private MusicPlayer musicPlayer = MusicPlayer.getInstance();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FolderFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FolderFragment.
     */
    public static FolderFragment newInstance(int columnCount) {
        FolderFragment fragment = new FolderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_folder, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.folder_list);
        setAdapter(recyclerView, songsRepository.getDirectoryTreeRoot());

        ImageView previous = view.findViewById(R.id.folder_back_button);
        previous.setOnClickListener(this);
        return view;
    }

    private void setAdapter(RecyclerView recyclerView, DirectoryTree directoryTree) {
        Context context = recyclerView.getContext();
        this.currentDirectoryTree = directoryTree;
        final List<Song> songList = directoryTree.getSongs();
        final List<DirectoryTree> subdirectoryList = directoryTree.getOrderedListOfSubdirectories();

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new FolderFragmentRecyclerViewAdapter(directoryTree, new OnListFragmentInteractionListener() {
            @Override
            public void onSongInteraction(Song song) {
                Log.d(TAG, "selected song position: " + songList.indexOf(song));
                Log.d(TAG, "selected song: " + song.getArtist() + " - " + song.getTitle());

                musicPlayer.createQueueFromSelectionAndPlay(getContext(), songList, songList.indexOf(song));
            }

            @Override
            public void onSubdirectoryInteraction(DirectoryTree subdirectoryItem) {
                Log.d(TAG, "selected subdirectory position: " + subdirectoryList.indexOf(subdirectoryItem));
                Log.d(TAG, "selected subdirectory: " + subdirectoryItem.getName() + " at " + subdirectoryItem.getPath());

                setAdapter(recyclerView, subdirectoryItem);
            }
        }));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void openParentFolder(View v) {
        DirectoryTree parent = currentDirectoryTree.getParent();
        if (parent == null) {
            return;
        }
        RecyclerView recyclerView = getView().findViewById(R.id.folder_list);
        setAdapter(recyclerView, parent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.folder_back_button:
                openParentFolder(v);
                break;
            default:
                throw new UnsupportedOperationException("No onClick action defined for " + v.getId());
        }
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
        void onSongInteraction(Song songItem);

        void onSubdirectoryInteraction(DirectoryTree subdirectoryItem);
    }
}
