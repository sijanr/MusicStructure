package com.example.android.musicstructure;

public class TrackInfo {


    private String mTrackURL;
    private String mArtistName;
    private String mTrackTitle;

    /**
     * Constructor to initialize the values of the class fields
     */
    public TrackInfo(String artistName, String trackTitle, String trackURL){
        mArtistName = artistName;
        mTrackTitle = trackTitle;
        mTrackURL = trackURL;
    }

    /**
     * Get the track URL
     * */
    public String getTrackURL() {
        return mTrackURL;
    }

    /**
     * Get the artist name for the track*/
    public String getArtistName() {
        return mArtistName;
    }

    /**
     * Get the track title*/
    public String getTrackTitle() {
        return mTrackTitle;
    }
}
