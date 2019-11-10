package de.elite.musicplayer;

import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import de.elite.musicplayer.ui.main.FolderFragment;
import de.elite.musicplayer.ui.main.PlayerFragment;
import de.elite.musicplayer.ui.main.PlaylistsFragment;
import de.elite.musicplayer.ui.main.QueueFragment;
import de.elite.musicplayer.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity implements QueueFragment.OnFragmentInteractionListener, PlayerFragment.OnFragmentInteractionListener, FolderFragment.OnFragmentInteractionListener, PlaylistsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onQueueFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPlayerFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFolderFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPlaylistFragmentInteraction(Uri uri) {

    }
}