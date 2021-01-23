package com.example.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private AudioManager audioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // AUDIOFOCUS_LOSS TRANSIENT means we have lost audio focus for a short amount of time
                // and AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK means we have lost audio focus
                // our app still continues to play song at lower volume but in both cases,
                // we want our app to pause playback and start it from beginning.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // it means we have gained focused and start playback
                mMediaPlayer.start();
            }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // it means we have completely lost the focus and we
                // have to stop the playback and free up the playback resources
                releaseMediaPlayer();
            }
        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<Word>();


        words.add(new Word("Where are you going?", "Kaha ja rhe ho ?", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "Kya namm hai ?", R.raw.phrase_what_is_your_name ));
        words.add(new Word("My name is...", "Mera nam hai ...",  R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "Kesa mehsus kar rhe  ho?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.", "Mai acha mehsus kar rah hu",  R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "Arah?",  R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.", "Ha",  R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.", "Aya", R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.", "chalo",  R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "aao",  R.raw.phrase_come_here));



        WordAdapter itemsAdapter = new WordAdapter (this, android.R.layout.simple_list_item_1, words, R.color.category_phrases);

        ListView listView = (ListView) findViewById(R.id.Phrs);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word w = words.get(position);
                releaseMediaPlayer();
                int result = audioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //audioManager.registerMediaButtonEventReceiver(RemoteControlReceiver);
                    // we have audio focus now
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, w.getAudioAdress());
                    mMediaPlayer.start();

                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }


            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            // mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

            audioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}