package com.example.relaxationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MAINACTIVITYTAG";
    private static final long STARTING_TIME_MILISECONDS = 1200000;
    private TextView countdownText;
    private Button countdownButton;

    private Button stopRelaxation;

    private CountDownTimer countDownTimer;
    private long timeLeftMiliseconds; // 20 min
    private long endTime;
    private boolean timerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createSomeButtons();
    }

    public void createSomeButtons(){
        countdownText = findViewById(R.id.timerText);
        countdownButton = findViewById(R.id.timerButton);

        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });

        stopRelaxation = findViewById(R.id.stopRelaxation);
        stopRelaxation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                stopAudio();
            }
        });
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
        endTime = System.currentTimeMillis() + timeLeftMiliseconds;

        countdownButton.setText("Pause Timer");
            countDownTimer = new CountDownTimer(timeLeftMiliseconds, 1000) {
                @Override
                public void onTick(long l) {
                    timeLeftMiliseconds = l;
                    updateTimer();
                }

                @Override
                public void onFinish() {
                    timerRunning = false;
                    resetTimer(findViewById(R.id.resetButton));
                }
            }.start();
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

    public void resetTimer(View v) {
        countDownTimer.cancel();
        timeLeftMiliseconds = STARTING_TIME_MILISECONDS;
        updateTimer();
        timerRunning = false;
        countdownButton.setText("Start Timer");
    }


    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences SPstop = getSharedPreferences("Preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = SPstop.edit();

        editor.putLong("miliseconds", timeLeftMiliseconds);
        editor.putBoolean("running", timerRunning);
        editor.putLong("end", endTime);

        editor.apply();

        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        SharedPreferences SPstart = getSharedPreferences("Preferences", MODE_PRIVATE);
        timeLeftMiliseconds = SPstart.getLong("miliseconds", STARTING_TIME_MILISECONDS);
        timerRunning = SPstart.getBoolean("running", false);

        updateTimer();

        if(timerRunning){
            Log.d(TAG, "onStart if statement");
            endTime = SPstart.getLong("end", 0);
            timeLeftMiliseconds = endTime - System.currentTimeMillis();
            if(timeLeftMiliseconds < 0) {
                Log.d(TAG, "timeleft:" + timeLeftMiliseconds);
                timerRunning = false;
                timeLeftMiliseconds = 0;
                stopTime();
                updateTimer();
            }
            else{
                startTime();
            }
        }
    }

    public void playAudio(View v){
        Intent intent = new Intent(this, AudioService.class);
        intent.putExtra("pause","no");
        startService(intent);
    }
    public void pauseAudio(View v) {
        Intent intent = new Intent(this, AudioService.class);
        intent.putExtra("pause","yes");
        startService(intent);
    }
    private void stopAudio() {
        stopService(new Intent(this, AudioService.class));
    }
}