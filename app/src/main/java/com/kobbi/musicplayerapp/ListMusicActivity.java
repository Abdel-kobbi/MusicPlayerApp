package com.kobbi.musicplayerapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ListView;
import android.Manifest;

import androidx.core.app.ActivityCompat;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ListMusicActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION = 100;
    private List<Song> songList;
    private ListView lvSongs;

    SongsAdapter songsAdapter;

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

        songsAdapter = new SongsAdapter(getApplicationContext(), songList);

        lvSongs.setEmptyView(findViewById(R.id.lvEmpty));

        lvSongs.setAdapter(songsAdapter);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, REQUEST_PERMISSION);
            return;
        } else {
            // we have permission to read from external storage
            getSongs();
        }

        lvSongs.setOnItemClickListener((parent, view, position, id) -> {
            Intent openMusicPlayer = new Intent(getApplicationContext(), MainActivity.class);
            openMusicPlayer.putExtra("music", songList.get(position));
            startActivity(openMusicPlayer);
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSongs();
            }
        }
    }

    private void getSongs() {
        // read songs from external storage
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {

            int indexTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int indexArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int indexPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String title = songCursor.getString(indexTitle);
                String artist = songCursor.getString(indexArtist);
                String path = songCursor.getString(indexPath);
                songList.add(new Song(title, artist, path));
            } while (songCursor.moveToNext());

            songCursor.close();

            songsAdapter.notifyDataSetChanged();
        }
    }
}