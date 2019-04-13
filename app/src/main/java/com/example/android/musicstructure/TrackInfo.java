package com.example.android.musicstructure;

public class TrackInfo {

    private String mArtistName;
    private String mTrackTitle;
    private String mTrackURL;

    //constructor to initialize the values of the class fields
    public TrackInfo(String artistName, String trackTitle, String trackURL) {
        mArtistName = artistName;
        mTrackTitle = trackTitle;
        mTrackURL = trackURL;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public String getTrackTitle() {
        return mTrackTitle;
    }

    public String getTrackURL() {
        return mTrackURL;
    }
}
