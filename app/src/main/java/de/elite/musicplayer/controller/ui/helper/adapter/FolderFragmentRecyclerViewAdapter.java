package de.elite.musicplayer.controller.ui.helper.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.elite.musicplayer.R;
import de.elite.musicplayer.model.DirectoryTree;
import de.elite.musicplayer.model.Song;
import de.elite.musicplayer.controller.ui.main.fragments.FolderFragment;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Song} and makes a call to the
 * specified {@link FolderFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FolderFragmentRecyclerViewAdapter extends RecyclerView.Adapter<FolderFragmentRecyclerViewAdapter.ViewHolder> {

    private final FolderFragment.OnListFragmentInteractionListener mListener;
    private List<DirectoryTree> subdirectories;
    private List<Song> songs;

    public FolderFragmentRecyclerViewAdapter(DirectoryTree directoryTree, FolderFragment.OnListFragmentInteractionListener listener) {
        mListener = listener;
        subdirectories = directoryTree.getOrderedListOfSubdirectories();
        songs = directoryTree.getSongs();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position < subdirectories.size()) {
            DirectoryTree subdirectory = subdirectories.get(position);
            holder.isDirectory = true;
            holder.mSubdirectory = subdirectory;
            holder.mIdView.setText(subdirectory.getName());
            holder.mContentView.setText(subdirectory.getPath());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onSubdirectoryInteraction(holder.mSubdirectory);
                    }
                }
            });
        }
        if (position >= subdirectories.size()) {
            Song song = songs.get(subdirectories.size() + position);
            holder.isDirectory = false;
            holder.mSong = song;
            holder.mIdView.setText(song.getTitle());
            holder.mContentView.setText(song.getArtist());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onSongInteraction(holder.mSong);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return subdirectories.size() + songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public boolean isDirectory;
        public Song mSong;
        public DirectoryTree mSubdirectory;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.song_title);
            mContentView = (TextView) view.findViewById(R.id.song_artist);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
