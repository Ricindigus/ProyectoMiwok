package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    ArrayList<ItemMiwok> colors;
    ListView lista;
    MiwokAdapter miwokAdapter;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeData();
        conectarVistas();
        miwokAdapter = new MiwokAdapter(this,colors,R.color.category_colors);
        lista.setAdapter(miwokAdapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                releaseMediaPlayer();
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    reproducirAudio(colors.get(position).getAudioRecurso());
                }
            }
        });

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        mediaPlayer.start();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS:
                        releaseMediaPlayer();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                        break;
                }
            }
        };

    }

    private void reproducirAudio(int recursoAudio){
        mediaPlayer = MediaPlayer.create(ColorsActivity.this,recursoAudio);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                releaseMediaPlayer();
            }
        });
        mediaPlayer.start();
    }
    private void releaseMediaPlayer(){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (audioManager != null) audioManager.abandonAudioFocus(onAudioFocusChangeListener);
    }

    private void conectarVistas() {
        lista = findViewById(R.id.list);
    }

    private void initializeData() {
        colors = new ArrayList<>();
        colors.add(new ItemMiwok("red","weṭeṭṭi",R.drawable.color_red,R.raw.color_red));
        colors.add(new ItemMiwok("green","chokokki",R.drawable.color_green,R.raw.color_green));
        colors.add(new ItemMiwok("brown","ṭakaakki",R.drawable.color_brown,R.raw.color_brown));
        colors.add(new ItemMiwok("gray","ṭopoppi",R.drawable.color_gray,R.raw.color_gray));
        colors.add(new ItemMiwok("black","kululli",R.drawable.color_black,R.raw.color_black));
        colors.add(new ItemMiwok("white","kelelli",R.drawable.color_white,R.raw.color_white));
        colors.add(new ItemMiwok("dusty yellow","ṭopiisә",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        colors.add(new ItemMiwok("mustard yellow","chiwiiṭә",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();

    }
}
