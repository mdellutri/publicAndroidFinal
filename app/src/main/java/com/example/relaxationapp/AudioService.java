package com.example.relaxationapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

//used this video to help develop the audio service https://www.youtube.com/watch?v=p2ffzsCqrs8

public class AudioService extends Service {
    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //moved the play function in here
        if(player == null){
            player = MediaPlayer.create(this, R.raw.relaxation);
        }
        String pause = intent.getStringExtra("pause");
        if(pause.equals("yes")){
            player.pause();
        }
        else if(pause.equals("no")){
            player.start();
        }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }

}
