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
import com.example.android.miwok.activities.NumbersActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    ArrayList<ItemMiwok> numbers;
    ListView lista;
    MiwokAdapter miwokAdapter;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
        lista = (ListView) rootView.findViewById(R.id.list);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeData();
        miwokAdapter = new MiwokAdapter(getActivity(),numbers,R.color.category_numbers);
        lista.setAdapter(miwokAdapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                releaseMediaPlayer();
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    reproducirAudio(numbers.get(position).getAudioRecurso());
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
        numbers = new ArrayList<>();
        numbers.add(new ItemMiwok("one","lutti",R.drawable.number_one,R.raw.number_one));
        numbers.add(new ItemMiwok("two","otiiko",R.drawable.number_two,R.raw.number_two));
        numbers.add(new ItemMiwok("three","tolookosu",R.drawable.number_three,R.raw.number_three));
        numbers.add(new ItemMiwok("four","oyyisa",R.drawable.number_four,R.raw.number_four));
        numbers.add(new ItemMiwok("five","massokka",R.drawable.number_five,R.raw.number_five));
        numbers.add(new ItemMiwok("six","temmokka",R.drawable.number_six,R.raw.number_six));
        numbers.add(new ItemMiwok("seven","kenekaku",R.drawable.number_seven,R.raw.number_seven));
        numbers.add(new ItemMiwok("eight","kawinta",R.drawable.number_eight,R.raw.number_eight));
        numbers.add(new ItemMiwok("nine","wo’e",R.drawable.number_nine,R.raw.number_nine));
        numbers.add(new ItemMiwok("ten","na’aacha",R.drawable.number_ten,R.raw.number_ten));
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
