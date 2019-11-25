package com.example.relaxationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MediaPlayer player;
    private TextView countdownText;
    private Button countdownButton;

    private CountDownTimer countDownTimer;
    private long timeLeftMiliseconds = 1200000; // 20 min
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countdownText = findViewById(R.id.timerText);
        countdownButton = findViewById(R.id.timerButton);

        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });

        updateTimer();
    }

    public void startStop() {
        if(timerRunning){
            stopTime();
        }
        else {
            startTime();
        }
    }

    public void startTime() {
        countDownTimer = new CountDownTimer(timeLeftMiliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftMiliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();

        countdownButton.setText("Pause Timer");
        timerRunning = true;
    }

    public void stopTime() {
        countDownTimer.cancel();
        countdownButton.setText("Start Timer");
        timerRunning = false;
    }

    public void updateTimer() {
        int minutes = (int) timeLeftMiliseconds / 60000;
        int seconds = (int) timeLeftMiliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownText.setText(timeLeftText);
    }

    public void play(View v) {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.relaxation);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopPlayer();
                }
            });
        }

        player.start();


    }

    public void pause(View v) {
        if (player != null) {
            player.pause();
        }
    }

    public void stop(View v) {
        stopPlayer();
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
            Toast.makeText(this, "MediaPlayer released", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}
