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

public class FamilyActivity extends AppCompatActivity {
    ArrayList<ItemMiwok> families;
    ListView lista;
    MiwokAdapter miwokAdapter;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeData();
        conectarVistas();
        miwokAdapter = new MiwokAdapter(this,families,R.color.category_family);
        lista.setAdapter(miwokAdapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                releaseMediaPlayer();
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    reproducirAudio(families.get(position).getAudioRecurso());
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
        mediaPlayer = MediaPlayer.create(FamilyActivity.this,recursoAudio);
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
        families = new ArrayList<>();
        families.add(new ItemMiwok("father","әpә",R.drawable.family_father,R.raw.family_father));
        families.add(new ItemMiwok("mother","әṭa",R.drawable.family_mother,R.raw.family_mother));
        families.add(new ItemMiwok("son","angsi",R.drawable.family_son,R.raw.family_son));
        families.add(new ItemMiwok("daughter","tune",R.drawable.family_daughter,R.raw.family_daughter));
        families.add(new ItemMiwok("older brother","taachi",R.drawable.family_older_brother,R.raw.family_older_brother));
        families.add(new ItemMiwok("younger brother","chalitti",R.drawable.family_older_sister,R.raw.family_younger_brother));
        families.add(new ItemMiwok("older sister","teṭe",R.drawable.family_younger_brother,R.raw.family_older_sister));
        families.add(new ItemMiwok("younger sister","kolliti",R.drawable.family_older_sister,R.raw.family_younger_sister));
        families.add(new ItemMiwok("grandmother","ama",R.drawable.family_grandmother,R.raw.family_grandmother));
        families.add(new ItemMiwok("grandfather","paapa",R.drawable.family_grandfather,R.raw.family_grandfather));


    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();

    }
}
