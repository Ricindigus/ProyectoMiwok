package com.example.android.miwok.activities;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.miwok.R;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

    ArrayList<ItemMiwok> phrases;
    ListView lista;
    MiwokAdapter miwokAdapter;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeData();
        conectarVistas();
        miwokAdapter = new MiwokAdapter(this,phrases,R.color.category_phrases);
        lista.setAdapter(miwokAdapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                releaseMediaPlayer();
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    reproducirAudio(phrases.get(position).getAudioRecurso());
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
        mediaPlayer = MediaPlayer.create(PhrasesActivity.this,recursoAudio);
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
        phrases = new ArrayList<>();
        phrases.add(new ItemMiwok("Where are you going?" ,"minto wuksus",0,R.raw.phrase_where_are_you_going));
        phrases.add(new ItemMiwok("What is your name?" ,"tinnә oyaase'nә",0,R.raw.phrase_what_is_your_name));
        phrases.add(new ItemMiwok("My name is..." ,"oyaaset...",0,R.raw.phrase_my_name_is));
        phrases.add(new ItemMiwok( "How are you feeling?" ,"michәksәs?",0,R.raw.phrase_how_are_you_feeling));
        phrases.add(new ItemMiwok("I’m feeling good.","kuchi achit",0,R.raw.phrase_im_feeling_good));
        phrases.add(new ItemMiwok("Are you coming?" ,"әәnәs'aa?",0,R.raw.phrase_are_you_coming));
        phrases.add(new ItemMiwok("Yes, I’m coming." ,"hәә’ әәnәm",0,R.raw.phrase_yes_im_coming));
        phrases.add(new ItemMiwok("I’m coming." ,"әәnәm",0,R.raw.phrase_im_coming));
        phrases.add(new ItemMiwok("Let’s go." ,"yoowutis",0,R.raw.phrase_lets_go));
        phrases.add(new ItemMiwok("Come here." ,"әnni'nem",0,R.raw.phrase_come_here));
    }

    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
