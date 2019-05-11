package com.example.android.musicstructure;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = findViewById(R.id.fab);


        // add viewpager to the activity
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        //create an adapter for the viewpager
        TrackPagerAdapter trackPagerAdapter = new TrackPagerAdapter(getSupportFragmentManager());

        //set the adpater
        viewPager.setAdapter(trackPagerAdapter);

        // hide or show FAB as user scrolls through the pages
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

                switch (i) {
                    case 0:
                        fab.show();
                        break;
                    case 1:
                        fab.hide();
                        break;
                    default:
                        fab.show();
                        break;
                }

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    /**
     * Create the menu items for the app
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Take user to the app's github page when the menu option is selected
     */
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
}
