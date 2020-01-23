package de.elite.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.elite.musicplayer.ui.main.QueueFragment;

public class QueueFragmentRecyclerViewAdapter extends RecyclerView.Adapter<QueueFragmentRecyclerViewAdapter.ViewHolder>{

    private final List<Song> mValues;
    private final QueueFragment.OnListFragmentInteractionListener mListener;

    public QueueFragmentRecyclerViewAdapter(List<Song> songList, QueueFragment.OnListFragmentInteractionListener listener) {
        mValues = songList;
        mListener = listener;
    }

    @Override
    public QueueFragmentRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.queue_fragment_item, parent, false);
        return new QueueFragmentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final QueueFragmentRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTitle());
        holder.mContentView.setText(mValues.get(position).getArtist());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Song mItem;

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
