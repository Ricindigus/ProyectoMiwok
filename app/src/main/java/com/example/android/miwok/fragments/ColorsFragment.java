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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    ArrayList<ItemMiwok> colors;
    ListView lista;
    MiwokAdapter miwokAdapter;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;

    public ColorsFragment() {
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
        miwokAdapter = new MiwokAdapter(getActivity(),colors,R.color.category_colors);
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
    public void onStop() {
        super.onStop();
        Log.i("COLORS", "onStop");
        releaseMediaPlayer();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
                Log.d("COLORS", "No visible.  Detener el audio.");
                // TODO stop audio playback
                releaseMediaPlayer();
            }
        }
    }
}
