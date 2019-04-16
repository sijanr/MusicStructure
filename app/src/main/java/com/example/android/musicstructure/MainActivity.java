package com.example.android.musicstructure;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TrackAdapter mTrackAdapter;
    private ArrayList<TrackInfo> mTrackList = new ArrayList<>();
    private MediaPlayer mMediaPlayer;
    private String mTrackTitle;
    private String mTrackAuthor;

    //current play/pause image view
    private ImageView currentView;

    //current position of the recycler view item
    private int currentPosition = -1;

    //handles audio focus
    private AudioManager mAudioManager;

    //audio focus listener when audio focus changes
    private AudioManager.OnAudioFocusChangeListener audioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            if (i == AudioManager.AUDIOFOCUS_GAIN) {
                mMediaPlayer.start();
            } else if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mMediaPlayer.pause();
            } else if (i == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };

    //handle any error while playing audio
    private MediaPlayer.OnErrorListener mediaErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            Toast.makeText(MainActivity.this, "Play failed! Check your network connection and try again", Toast.LENGTH_LONG).show();
            releaseMediaPlayer();
            return true;
        }
    };

    //handle audio focus when media has completed playing
    private MediaPlayer.OnCompletionListener mediaCompleteListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    //notify when the media is prepared
    private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            currentView.setImageResource(R.drawable.ic_pause_button);
            mediaPlayer.start();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //share the current track info when like is clicked
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTrackAuthor != null && mTrackTitle != null) {
                    String shareText = "Listening '" + mTrackTitle + "' by '" + mTrackAuthor + "'";
                    ShareCompat.IntentBuilder.from(MainActivity.this)
                            .setType("text/plain")
                            .setChooserTitle("Share the track info with")
                            .setText(shareText)
                            .startChooser();
                } else {
                    Toast.makeText(MainActivity.this, "Select a track first to share", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //add tracks to the list
        mTrackList.add(new TrackInfo("Broke For Free", "Night Owl", "https://freemusicarchive.org/file/music/WFMU/Broke_For_Free/Directionless_EP/Broke_For_Free_-_01_-_Night_Owl.mp3"));
        mTrackList.add(new TrackInfo("Tours", "Enthusiast", "https://freemusicarchive.org/file/music/no_curator/Tours/Enthusiast/Tours_-_01_-_Enthusiast.mp3"));
        mTrackList.add(new TrackInfo("The Kyoto", "Hachiko", "https://freemusicarchive.org/file/music/ccCommunity/The_Kyoto_Connection/Wake_Up/The_Kyoto_Connection_-_09_-_Hachiko_The_Faithtful_Dog.mp3"));
        mTrackList.add(new TrackInfo("Podington Bear", "Starling", "https://freemusicarchive.org/file/music/Music_for_Video/Podington_Bear/Solo_Instruments/Podington_Bear_-_Starling.mp3"));
        mTrackList.add(new TrackInfo("Scott Holmes", "Hopeful Journey", "https://freemusicarchive.org/file/music/no_curator/Scott_Holmes/Corporate__Motivational_Music_2/Scott_Holmes_-_02_-_Hopeful_Journey.mp3"));
        mTrackList.add(new TrackInfo("Nctrnm", "XY", "https://freemusicarchive.org/file/music/ccCommunity/Nctrnm/N3/Nctrnm_-_01_-_XY.mp3"));
        mTrackList.add(new TrackInfo("Scott Holmes", "Together We Stand", "https://freemusicarchive.org/file/music/no_curator/Scott_Holmes/Documentary__TV_series/Scott_Holmes_-_02_-_Together_We_Stand.mp3"));
        //get a handle to the recycler view
        mRecyclerView = findViewById(R.id.recycler_view);

        //create the adapter to supply data to recycler view
        mTrackAdapter = new TrackAdapter(mTrackList);

        //attach the adapter to the recycler view
        mRecyclerView.setAdapter(mTrackAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //change FAB visibility when the user scrolls through the list of tracks
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });


        //handle item click listener
        mTrackAdapter.setOnItemClickListener(new TrackAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position, ImageView itemView) {
                //pause and play the selected track
                if (mMediaPlayer != null && currentPosition == position) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        mAudioManager.abandonAudioFocus(audioFocusListener);
                        currentView.setImageResource(R.drawable.ic_play_button);
                    } else {
                        //request audio focus
                        int requestFocus = mAudioManager.requestAudioFocus(audioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                        //play the music if the request was successful
                        if (requestFocus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                            currentView.setImageResource(R.drawable.ic_pause_button);
                            mMediaPlayer.start();
                        }
                    }
                }

                //play a new track
                else {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.stop();
                        releaseMediaPlayer();
                    }
                    currentView = itemView;
                    currentPosition = position;
                    //request audio focus
                    int requestFocus = mAudioManager.requestAudioFocus(audioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                    //play the music if the request was successful
                    if (requestFocus == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                        mMediaPlayer = new MediaPlayer();
                        mMediaPlayer.setOnErrorListener(mediaErrorListener);
                        mMediaPlayer.setOnPreparedListener(preparedListener);
                        //play the track or show play failed message
                        try {
                            mMediaPlayer.setDataSource(mTrackList.get(position).getTrackURL());
                        } catch (IOException exception) {
                            Toast.makeText(MainActivity.this, "Check the URL and try again", Toast.LENGTH_SHORT).show();
                            releaseMediaPlayer();
                        } catch (IllegalArgumentException illegalException) {
                            Toast.makeText(MainActivity.this, "Check the URL and try again", Toast.LENGTH_SHORT).show();
                            releaseMediaPlayer();
                        }
                        mMediaPlayer.prepareAsync();
                        mTrackAuthor = mTrackList.get(position).getArtistName();
                        mTrackTitle = mTrackList.get(position).getTrackTitle();
                        mMediaPlayer.setOnCompletionListener(mediaCompleteListener);
                    }
                }
            }
        });

    }

    //release media player when activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //take user to the app's github page when the option is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item:
                Uri webpage = Uri.parse("https://github.com/sijanr/MusicStructure");
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (getPackageManager() != null) {
                    startActivity(intent);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //release the media player and abandon audio focus
    private void releaseMediaPlayer() {
        currentView.setImageResource(R.drawable.ic_play_button);
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(audioFocusListener);
        }
    }
}
