package com.android.alarmy_test2.CountdownVsTimer;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.alarmy_test2.R;

import java.util.Timer;
import java.util.TimerTask;

public class CountdownFragment extends Fragment {

    private MediaPlayer mediaPlayer;
    private TextView tv_stopwatch, tv_stopwatch_millis;
    private Button btn_start, btn_pause_resume, btn_reset_lap;
    private ProgressBar progressBar;
    private long progress = 0;
    private boolean mTimeRunning = false;
    private Timer timer;
    private TimerTask timerTask;
    private long time = 0;
    private TableLayout tableLayout;
    private String rowString;
    private long lastTimeMillis = 0;
    private int lap_index = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_countdowm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_stopwatch = view.findViewById(R.id.text_view_stopwatch);
        tv_stopwatch_millis = view.findViewById(R.id.text_view_stopwatch_millis);
        progressBar = view.findViewById(R.id.progressBar_stopwatch);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            progressBar.setMin(0);
        }
        progressBar.setMax(60);
        btn_pause_resume = view.findViewById(R.id.btn_pause_resume_stopwatch);
        btn_reset_lap = view.findViewById(R.id.btn_reset_lap_stopwatch);
        btn_start = view.findViewById(R.id.btn_start_stopwatch);
        tableLayout = view.findViewById(R.id.tablelayout);

        timer = new Timer();

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.ringtone);
        btn_start.setVisibility(View.VISIBLE);
        btn_pause_resume.setVisibility(View.INVISIBLE);
        btn_reset_lap.setVisibility(View.INVISIBLE);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStopwatch();
            }
        });

        btn_reset_lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopwatchResetLap();
            }
        });

        btn_pause_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopwatchPauseResume();
            }
        });
    }
    private void stopwatchPauseResume() {
        if (!mTimeRunning) {
            mTimeRunning = true;
            start();
        } else {
            mediaPlayer.pause();
            timerTask.cancel();
            btn_reset_lap.setText("RESET");
            btn_pause_resume.setText("RESUME");
            mTimeRunning = false;
        }
    }

    private void stopwatchResetLap() {
        if (!mTimeRunning) {
            mediaPlayer.pause();
            timerTask.cancel();
            time = 0;
            progress = 0;
            updateStopwatchTextView();
            btn_start.setVisibility(View.VISIBLE);
            btn_reset_lap.setVisibility(View.INVISIBLE);
            btn_pause_resume.setVisibility(View.INVISIBLE);
            mTimeRunning = false;
            removeStopwatchHistory();
        } else {
            addStopwatchHistory(getRowString());
        }
    }

    private String getRowString() {
        long rounded;
        int minutes, seconds, millis;
        rounded = (time);
        minutes = (int) (rounded / 100 / 60);
        seconds = (int) (rounded / 100 % 60);
        millis = (int) (rounded - (minutes * 60 * 100 + seconds * 100));
        progress = seconds;
        String indexFormmated = String.format("%02d", lap_index);
        lap_index++;
        String MinSecFormatted = String.format("%02d:%02d:", minutes, seconds);
        String MillisFormatted = String.format("%02d", millis);


        rowString = "     " + indexFormmated + "\t\t\t\t\t\t\t\t\t" + MinSecFormatted + MillisFormatted + "\t\t\t\t\t" + calcdifference(rounded, lastTimeMillis);

        lastTimeMillis = rounded;

        return rowString;
    }

    private String calcdifference(long rounded, long lastTimeMillis) {
        long difference = rounded - lastTimeMillis;
        int minutes;
        int seconds;
        int millis;
        minutes = (int) (difference / 100 / 60);
        seconds = (int) (difference / 100 % 60);
        millis = (int) (difference - (minutes * 60 * 100 + seconds * 100));

        return String.format("%02d:%02d:", minutes, seconds) + String.format("%02d", millis);
    }

    private void addStopwatchHistory(String row_string) {
        TableRow tableRow = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(lp);

        TextView index = new TextView(getActivity());
        index.setText(row_string);
        index.setTextSize(24);
        tableRow.addView(index);

        tableLayout.addView(tableRow);
    }

    private void removeStopwatchHistory() {
        int count = tableLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow)
                ((ViewGroup) child).removeAllViews();
        }
    }


    private void startStopwatch() {
        if (!mTimeRunning) {
            mTimeRunning = true;
            start();
        }
    }

    private void start() {
        btn_start.setVisibility(View.INVISIBLE);
        btn_pause_resume.setVisibility(View.VISIBLE);
        btn_reset_lap.setVisibility(View.VISIBLE);

        btn_reset_lap.setText("LAP");
        btn_pause_resume.setText("PAUSE");

        lap_index = 1;
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        updateStopwatchTextView();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 10);
    }

    private void updateStopwatchTextView() {
        long rounded;
        int minutes, seconds, millis;
        rounded = time;
        minutes = (int) ((rounded / 100) / 60);
        seconds = (int) ((rounded / 100) % 60);
        millis = (int) (rounded - (minutes * 60 * 100 + seconds * 100));
        progress = seconds;
        tv_stopwatch.setText(String.format("%02d:%02d:", minutes, seconds));
        tv_stopwatch_millis.setText(String.format("%02d", millis));
        progressBar.setProgress((int) progress);
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.pause();
    }

}
