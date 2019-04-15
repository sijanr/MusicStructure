package com.example.android.musicstructure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private ArrayList<TrackInfo> mTrackList = new ArrayList<>();
    private OnClickListener mListener;

    public TrackAdapter(ArrayList<TrackInfo> trackList) {
        mTrackList = trackList;
    }

    //recycler view item click listener interface
    public interface OnClickListener {
        void onItemClick(int position, ImageView itemView);
    }

    public void setOnItemClickListener(OnClickListener listener) {
        mListener = listener;
    }

    static class TrackViewHolder extends RecyclerView.ViewHolder {
        public final TextView artistName;
        public final TextView trackTitle;
        public ImageView playPause;

        public TrackViewHolder(final View itemView, final OnClickListener listener) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artist_name_text_view);
            trackTitle = itemView.findViewById(R.id.track_name_text_view);
            playPause = itemView.findViewById(R.id.play_button_image_view);

            //handle view click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, playPause);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public TrackAdapter.TrackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View mItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.template_layout, viewGroup, false);
        return new TrackViewHolder(mItemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final TrackAdapter.TrackViewHolder trackViewHolder, int i) {
        trackViewHolder.trackTitle.setText(mTrackList.get(i).getTrackTitle());
        trackViewHolder.artistName.setText(mTrackList.get(i).getArtistName());

    }

    @Override
    public int getItemCount() {
        return mTrackList.size();
    }
}
