package com.example.android.musicstructure;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackLikeFragment extends Fragment {

    // text views for template_layout_like.xml file
    private TextView title;
    private TextView author;

    private final String MyPREFERENCES = "MyPrefs";

    public TrackLikeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate the view for the fragment
        View rootView = inflater.inflate(R.layout.template_layout_like, container, false);

        //initialize the views with their ids
        title = rootView.findViewById(R.id.like_track_title);
        author = rootView.findViewById(R.id.like_track_artist);

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();

        //get the recently liked track info from the track list fragment and set the textviews on the information received
        SharedPreferences prefs = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String restoredTitle = prefs.getString("TAG_NAME", null);
        String restoredArtist = prefs.getString("TAG_ARTIST", null);
        title.setText(restoredTitle);
        author.setText(restoredArtist);
    }


}
