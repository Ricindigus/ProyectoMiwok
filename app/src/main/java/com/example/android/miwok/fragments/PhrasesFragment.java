package com.example.android.miwok.fragments;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.miwok.R;
import com.example.android.miwok.activities.ItemMiwok;
import com.example.android.miwok.activities.MiwokAdapter;
import com.example.android.miwok.activities.PhrasesActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {
    ArrayList<ItemMiwok> phrases;
    ListView lista;
    MiwokAdapter miwokAdapter;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;


    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_layout, container, false);
        lista = rootView.findViewById(R.id.list);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeData();
        miwokAdapter = new MiwokAdapter(getActivity(),phrases,R.color.category_phrases);
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
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);


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
        mediaPlayer = MediaPlayer.create(getActivity(),recursoAudio);
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

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (!isVisibleToUser) releaseMediaPlayer();
        }
    }
}
