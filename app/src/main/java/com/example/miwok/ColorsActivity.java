package com.example.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_colors);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("red", "laal", R.drawable.color_red, R.raw.color_red));  // resource id is of integer type
        words.add(new Word("green", "hara", R.drawable.color_green,  R.raw.color_green));
        words.add(new Word("brown", "bhura", R.drawable.color_brown,  R.raw.color_brown));
        words.add(new Word("gray", "gray", R.drawable.color_gray,  R.raw.color_gray));
        words.add(new Word("black","kala", R.drawable.color_black,  R.raw.color_black));
        words.add(new Word("white", "safed", R.drawable.color_white,  R.raw.color_white));
        words.add(new Word("dusty yellow", "peela", R.drawable.color_dusty_yellow,  R.raw.color_dusty_yellow));
        words.add(new Word("mustard yellow", "sarso pela", R.drawable.color_mustard_yellow,  R.raw.color_mustard_yellow));

        WordAdapter itemsAdapter = new WordAdapter (this, android.R.layout.simple_list_item_1,words, R.color.category_colors);

        ListView listView = (ListView) findViewById(R.id.col);

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
                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, w.getAudioAdress());
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