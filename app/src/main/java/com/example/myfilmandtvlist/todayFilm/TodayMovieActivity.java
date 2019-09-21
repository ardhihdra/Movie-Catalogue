package com.example.myfilmandtvlist.todayFilm;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.todayFilm.ui.todaymovie.TodayMovieFragment;

public class TodayMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_movie_activity);

        AppCompatActivity c = TodayMovieActivity.this;
        if (c.getSupportActionBar() != null) {
            c.getSupportActionBar().setTitle("Today Released Movie");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TodayMovieFragment.newInstance())
                    .commitNow();
        }
    }
}
