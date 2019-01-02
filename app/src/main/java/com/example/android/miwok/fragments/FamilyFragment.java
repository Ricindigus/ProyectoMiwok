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
import com.example.android.miwok.activities.FamilyActivity;
import com.example.android.miwok.activities.ItemMiwok;
import com.example.android.miwok.activities.MiwokAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {
    ArrayList<ItemMiwok> families;
    ListView lista;
    MiwokAdapter miwokAdapter;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;

    public FamilyFragment() {
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

        miwokAdapter = new MiwokAdapter(getActivity(),families,R.color.category_family);
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
