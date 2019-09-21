package com.example.myfilmandtvlist.reminder;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.movie.Movie;
import com.example.myfilmandtvlist.todayFilm.ui.todaymovie.TodayMovieViewModel;

import java.util.ArrayList;

import static com.example.myfilmandtvlist.reminder.ReminderReceiver.DAILY_MESSAGE;
import static com.example.myfilmandtvlist.reminder.ReminderReceiver.ID_DAILY_REMINDER;

public class ReminderActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Switch dailySwitch, releaseSwitch;
        final ReminderReceiver mReminderReceiver = new ReminderReceiver();
        final ReminderPreference mReminderPreference = new ReminderPreference(this);

        dailySwitch = findViewById(R.id.alarm_switch);
        releaseSwitch = findViewById(R.id.release_today_switch);
        if(mReminderPreference.getDailyRemainder()) {
            dailySwitch.setChecked(true);
        } else {
            dailySwitch.setChecked(false);
        }

        if(mReminderPreference.getReleaseRemainder()) {
            releaseSwitch.setChecked(true);
        } else {
            releaseSwitch.setChecked(false);
        }

        dailySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // isChecked true if the switch is in the On position
                if(isChecked) {
                    mReminderPreference.setDailyStatus(true);
                    mReminderReceiver.setDailyAlarm(ReminderActivity.this, ID_DAILY_REMINDER, DAILY_MESSAGE);
                } else {
                    mReminderPreference.setDailyStatus(false);
                    mReminderReceiver.cancelAlarm(ReminderActivity.this, ID_DAILY_REMINDER);
                }
            }
        });

        releaseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    final ArrayList<Movie> listTodayMovie = new ArrayList<>();
                    TodayMovieViewModel todayMovieViewModel = ViewModelProviders.of(ReminderActivity.this).get(TodayMovieViewModel.class);
                    todayMovieViewModel.setTodayMovies();

                    mReminderPreference.setReleaseStatus(true);
                    todayMovieViewModel.getTodayMovies().observe(ReminderActivity.this, new Observer<ArrayList<Movie>>() {
                        @Override
                        public void onChanged(@Nullable ArrayList<Movie> movies) {
                            listTodayMovie.clear();
                            listTodayMovie.addAll(movies);
                            int id = ReminderReceiver.ID_RELEASE_REMINDER;
                            for(int i = 0; i < listTodayMovie.size(); i++) {
                                Log.d("tes overview","---------------------Movie overview : "+listTodayMovie.get(i).getName());
                                mReminderReceiver.setDailyAlarm(ReminderActivity.this, id, listTodayMovie.get(i).getName());
                                id++;
                                if (i > 3) i = listTodayMovie.size();
                            }
                        }
                    });
                } else {
                    mReminderPreference.setReleaseStatus(false);
                    mReminderReceiver.cancelAlarm(ReminderActivity.this, ReminderReceiver.ID_RELEASE_REMINDER);
                }
            }
        });
    }
}
