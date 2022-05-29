package com.android.alarmy_test2.CountdownVsTimer;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.alarmy_test2.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CountdownTimerActivity extends Activity {
    private long START_IN_TIME_MILLIS;
    private TextView tv_countdown, tv_countdown_second;
    private Button btn_restart,btn_pause,btn_resume,btn_cancel;
    private ProgressBar progressBar;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimerLeftMillis;
    private long progress;
    private SimpleDateFormat formatter;
    private Date date;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counttimer);

        initUI();

        updateCountdownTimerText();
        startCountdownTimer(START_IN_TIME_MILLIS);
        progressBar.setProgressDrawable(getDrawable(R.drawable.circul));
        progressBar.setProgress(0);
        progressBar.setProgress((int)(progress));

        btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resumeTimer();
            }
        });

        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartTimer();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTimer();
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
            }
        });
    }

    private void pauseTimer() {
        if(mTimerRunning){
            mCountDownTimer.cancel();
            mTimerRunning = false;
            btn_pause.setVisibility(View.INVISIBLE);
            btn_resume.setVisibility(View.VISIBLE);
            btn_restart.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.INVISIBLE);
            progressBar.setProgressDrawable(getDrawable(R.drawable.circul));
            progressBar.setProgress(0);
            progressBar.setProgress((int) (progress-1000));
        }
    }

    private void cancelTimer() {
        finish();
    }

    private void restartTimer() {
        if(mTimerLeftMillis!=START_IN_TIME_MILLIS){
            mTimerLeftMillis = START_IN_TIME_MILLIS;
            progress = mTimerLeftMillis;
            updateCountdownTimerText();
            btn_restart.setVisibility(View.INVISIBLE);
            btn_cancel.setVisibility(View.INVISIBLE);
            btn_pause.setVisibility(View.INVISIBLE);
            btn_resume.setVisibility(View.INVISIBLE);
            progressBar.setProgress(0);
            progressBar.setProgress((int) progress);
            startCountdownTimer(START_IN_TIME_MILLIS);
            progressBar.setProgressDrawable(getDrawable(R.drawable.circul));
            progressBar.setProgress(0);
            progressBar.setProgress((int)(progress));
        }else{
            progressBar.setProgress(0);
            progressBar.setProgress((int)(progress));
        }

    }

    private void resumeTimer() {
        if(!mTimerRunning){
            startCountdownTimer(mTimerLeftMillis);
            progressBar.setProgressDrawable(getDrawable(R.drawable.circul));
            progressBar.setProgress(0);
            progressBar.setProgress((int)(progress));
        }
    }

    private void startCountdownTimer(long start_in_time_millis) {
        long millis = System.currentTimeMillis();
        date = new Date(millis+start_in_time_millis);

        mCountDownTimer = new CountDownTimer(mTimerLeftMillis,1000) {
            @Override
            public void onTick(long l) {
                mTimerLeftMillis = l;
                progress = mTimerLeftMillis;
                updateCountdownTimerText();
                progressBar.setProgress((int) (progress-1000));
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                mTimerRunning = false;
                finish();
            }
        }.start();

        mTimerRunning = true;
        btn_pause.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.VISIBLE);
        btn_restart.setVisibility(View.INVISIBLE);
        btn_resume.setVisibility(View.INVISIBLE);
    }

    private void initUI() {

        Bundle bundle = getIntent().getExtras();
        START_IN_TIME_MILLIS = bundle.getLong("Millis Second");
        mTimerLeftMillis = START_IN_TIME_MILLIS;
        progress = mTimerLeftMillis;
        long millis = System.currentTimeMillis();
        formatter = new SimpleDateFormat("E, MMM dd yyyy 'at' hh:mm:ss aa");
        date = new Date(millis + START_IN_TIME_MILLIS);


        tv_countdown = (TextView) findViewById(R.id.text_view_countdown);
        tv_countdown_second = (TextView) findViewById(R.id.text_view_countdown_second);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_pause = (Button)findViewById(R.id.btn_pause);
        btn_restart = (Button)findViewById(R.id.btn_restart);
        btn_resume = (Button)findViewById(R.id.btn_resume);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            progressBar.setMin(0);
        }
        progressBar.setMax((int) START_IN_TIME_MILLIS);
        progressBar.setProgress((int) progress);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("My notification",
                    "My notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        btn_resume.setVisibility(View.INVISIBLE);
        btn_cancel.setVisibility(View.INVISIBLE);
        btn_pause.setVisibility(View.INVISIBLE);
        btn_resume.setVisibility(View.INVISIBLE);
    }

    private void updateCountdownTimerText(){
        int minutes = (int) ((mTimerLeftMillis/1000)/60);
        int seconds = (int) ((mTimerLeftMillis/1000)%60);
        int hours = minutes/60;

        tv_countdown.setText(String.format("%02d:%02d:",hours,minutes));
        tv_countdown_second.setText(String.format("%02d",seconds));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
