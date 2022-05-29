package com.android.alarmy_test2.CountdownVsTimer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.alarmy_test2.CountdownVsTimer.CountdownTimerActivity;
import com.android.alarmy_test2.NewsTags.HoroscopeFragment;
import com.android.alarmy_test2.NewsTags.SummaryFragment;
import com.android.alarmy_test2.NewsTags.TodayNewsFragment;
import com.android.alarmy_test2.NewsTags.WeatherFragment;
import com.android.alarmy_test2.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;



public class TimerFragment extends Fragment {

    private NumberPicker np_hour,np_minute,np_second;
    private Button btn_start;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        np_hour = view.findViewById(R.id.np_hour);
        np_minute = view.findViewById(R.id.np_minute);
        np_second = view.findViewById(R.id.np_second);
        btn_start = view.findViewById(R.id.btn_start_timer);
        setNumberPickerTimer();

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour, minute,second;
                long timeMillisSeconds = 0;

                hour = np_hour.getValue();
                minute = np_minute.getValue();
                second  =np_second.getValue();

                timeMillisSeconds = (hour*60*60) + (minute*60) + second;
                timeMillisSeconds = timeMillisSeconds*1000;

                if (timeMillisSeconds!=0){
                    Intent intent = new Intent(getActivity(), CountdownTimerActivity.class);
                    intent.putExtra("Millis Second",timeMillisSeconds);
                    startActivity(intent);
                }else
                    Toast.makeText(getActivity(),"Please pick a number",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setNumberPickerTimer() {
        String[] hours = getResources().getStringArray(R.array.spinner_hour);
        String[] minutes = getResources().getStringArray(R.array.spinner_minutes_seconds);
        String[] seconds = getResources().getStringArray(R.array.spinner_minutes_seconds);

        np_hour.setMinValue(0);
        np_minute.setMinValue(0);
        np_second.setMinValue(0);

        np_hour.setMaxValue(hours.length-1);
        np_minute.setMaxValue(minutes.length-1);
        np_second.setMaxValue(seconds.length-1);

        np_hour.setDisplayedValues(hours);
        np_minute.setDisplayedValues(minutes);
        np_second.setDisplayedValues(seconds);
    }
}
