package com.kobbi.musicplayerapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ListMusicActivity extends AppCompatActivity {

    private List<Song> songList;
    private ListView lvSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_music);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvSongs = findViewById(R.id.lvSongs);
        songList = new ArrayList<>();

        SongsAdapter songsAdapter = new SongsAdapter(getApplicationContext(), songList);

        lvSongs.setEmptyView(findViewById(R.id.lvEmpty));

        lvSongs.setAdapter(songsAdapter);
    }
}