package com.example.miwok;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_family);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("father", "papa", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "mammi", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("son", "beta", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "beti", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("older brother", "bhaiya", R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word("younger brother", "bhai", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word("older sister", "didi", R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word("younger sister", "behen", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word("grandmother ", "amma", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grandfather", "dada", R.drawable.family_grandfather, R.raw.family_grandfather));



        WordAdapter itemsAdapter = new WordAdapter (this, android.R.layout.simple_list_item_1, words, R.color.category_family);

        ListView listView = (ListView) findViewById(R.id.fam);

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
                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, w.getAudioAdress());
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