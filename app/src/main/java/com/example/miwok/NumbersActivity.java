package com.example.miwok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_numbers);
        //setTitle("Numbers");

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        final ArrayList<Word> nums = new ArrayList<Word>();

        nums.add(new Word("one", "Ek", R.drawable.number_one, R.raw.number_one));
        nums.add(new Word("Two", "Do", R.drawable.number_two, R.raw.number_two));
        nums.add(new Word("Three", "Tin", R.drawable.number_three,  R.raw.number_three));
        nums.add(new Word("Four", "Char", R.drawable.number_four,  R.raw.number_four));
        nums.add(new Word("Five", "Panch", R.drawable.number_five,  R.raw.number_five));
        nums.add(new Word("Six", "Chaen", R.drawable.number_six,  R.raw.number_six));
        nums.add(new Word("Seven", "Saat", R.drawable.number_seven,  R.raw.number_seven));
        nums.add(new Word("Eight", "Aath", R.drawable.number_eight,  R.raw.number_eight));
        nums.add(new Word("Nine", "Noo", R.drawable.number_nine,  R.raw.number_nine));
        nums.add(new Word("Ten", "Das", R.drawable.number_ten,  R.raw.number_ten));


        WordAdapter itemsAdapter = new WordAdapter(this, android.R.layout.simple_list_item_1, nums, R.color.category_numbers);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word w = nums.get(position);

                releaseMediaPlayer();
                int result = audioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //audioManager.registerMediaButtonEventReceiver(RemoteControlReceiver);
                    // we have audio focus now
                    mMediaPlayer = MediaPlayer.create(NumbersActivity.this, w.getAudioAdress());
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