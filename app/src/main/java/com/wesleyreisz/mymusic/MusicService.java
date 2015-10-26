package com.wesleyreisz.mymusic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by wesleyreisz on 10/25/15.
 */
public class MusicService extends Service {
    public static final String SONG_TO_START = "com.wesleyreisz.mymusic.song2start";
    private static int song = R.raw.watch_me;
    private static final String TAG = null;
    MediaPlayer player;
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(player!=null && player.isPlaying()) {
            onDestroy();
            onCreate();
        }

        song =  intent.getIntExtra(SONG_TO_START,R.raw.watch_me);
        player = MediaPlayer.create(this, song);
        player.setLooping(true); // Set looping
        player.setVolume(1.0f, 1.0f);
        player.start();
        Toast.makeText(this, "Started Song", Toast.LENGTH_LONG).show();

        return START_STICKY;
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {

    }
    public void onPause() {

    }
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }
}