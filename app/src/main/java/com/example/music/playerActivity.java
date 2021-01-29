package com.example.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class playerActivity extends AppCompatActivity {

    Button but_pause, but_next, but_previous;
    TextView songtextview;
    SeekBar songseekbar;
    static MediaPlayer mymediaplayer;
    int Position;
    ArrayList<File> mysongs;
    Thread updateseekbar;
    String sname;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        but_pause = (Button) findViewById(R.id.pause_button);
        but_next = (Button) findViewById(R.id.next_button);
        but_previous = (Button) findViewById(R.id.previous_button);
        songtextview = (TextView) findViewById(R.id.songtextview);
        songseekbar = (SeekBar) findViewById(R.id.seekbar);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        updateseekbar = new Thread() {

            @Override
            public void run() {
                int totalDuration = mymediaplayer.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = mymediaplayer.getCurrentPosition();
                        songseekbar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }


        };

        if (mymediaplayer != null) {
            mymediaplayer.stop();
            mymediaplayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mysongs = (ArrayList) bundle.getParcelableArrayList("songs");
        sname = mysongs.get(Position).getName().toString();
        final String songName = i.getStringExtra("songname");
        songtextview.setText(songName);
        songtextview.setSelected(true);

        Position = bundle.getInt("pos", 0);

        Uri u = Uri.parse(mysongs.get(Position).toString());
        mymediaplayer = MediaPlayer.create(getApplicationContext(), u);
        mymediaplayer.start();
        songseekbar.setMax(mymediaplayer.getDuration());

        updateseekbar.start();
        songseekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        songseekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        songseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mymediaplayer.seekTo(seekBar.getProgress());
            }
        });

        but_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songseekbar.setMax(mymediaplayer.getDuration());
                if (mymediaplayer.isPlaying()) {
                    but_pause.setBackgroundResource(R.drawable.play_icon);
                    mymediaplayer.pause();
                } else {
                    but_pause.setBackgroundResource(R.drawable.pause_icon);
                    mymediaplayer.start();
                }
            }
        });

        but_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mymediaplayer.stop();
                mymediaplayer.release();
                Position = ((Position + 1) >0) ? (mysongs.size()+1):(Position+1);
                Uri u = Uri.parse(mysongs.get(Position).toString());
                mymediaplayer = MediaPlayer.create(getApplicationContext(), u);
                sname = mysongs.get(Position).getName().toString();
                songtextview.setText(sname);
                mymediaplayer.start();
            }
        });

        but_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mymediaplayer.stop();
                mymediaplayer.release();
                Position = ((Position - 1) < 0) ? (mysongs.size() - 1) : (Position - 1);
                Uri u = Uri.parse(mysongs.get(Position).toString());
                mymediaplayer = MediaPlayer.create(getApplicationContext(), u);
                sname = mysongs.get(Position).getName().toString();
                songtextview.setText(sname);
                mymediaplayer.start();

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
         return super.onOptionsItemSelected(item);
    }
}