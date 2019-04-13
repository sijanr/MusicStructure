package com.example.android.musicstructure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private ArrayList<TrackInfo> mTrackList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public TrackAdapter(Context context, ArrayList<TrackInfo> trackList) {
        mLayoutInflater = LayoutInflater.from(context);
        mTrackList = trackList;
    }

    class TrackViewHolder extends RecyclerView.ViewHolder {
        public final TextView artistName;
        public final TextView trackTitle;
        final TrackAdapter mTrackAdapter;

        public TrackViewHolder(View itemView, TrackAdapter trackAdapter) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artist_name_text_view);
            trackTitle = itemView.findViewById(R.id.track_name_text_view);
            mTrackAdapter = trackAdapter;
        }
    }

    @NonNull
    @Override
    public TrackAdapter.TrackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mItemView = mLayoutInflater.inflate(R.layout.template_layout, viewGroup, false);
        return new TrackViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackAdapter.TrackViewHolder trackViewHolder, int i) {
        trackViewHolder.trackTitle.setText(mTrackList.get(i).getTrackTitle());
        trackViewHolder.artistName.setText(mTrackList.get(i).getArtistName());

    }

    @Override
    public int getItemCount() {
        return mTrackList.size();
    }
}
