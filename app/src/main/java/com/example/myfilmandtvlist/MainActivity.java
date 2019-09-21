package com.example.myfilmandtvlist;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myfilmandtvlist.favMovie.FavMovieFragment;
import com.example.myfilmandtvlist.favShow.FavTvShowFragment;
import com.example.myfilmandtvlist.movie.Movie;
import com.example.myfilmandtvlist.movie.MovieFragment;
import com.example.myfilmandtvlist.reminder.ReminderActivity;
import com.example.myfilmandtvlist.reminder.ReminderPreference;
import com.example.myfilmandtvlist.reminder.ReminderReceiver;
import com.example.myfilmandtvlist.searchMovie.SearchMovieFragment;
import com.example.myfilmandtvlist.searchShow.SearchShowFragment;
import com.example.myfilmandtvlist.todayFilm.ui.todaymovie.TodayMovieViewModel;
import com.example.myfilmandtvlist.tvShow.ShowFragment;

import java.util.ArrayList;

import static com.example.myfilmandtvlist.reminder.ReminderReceiver.DAILY_MESSAGE;
import static com.example.myfilmandtvlist.reminder.ReminderReceiver.ID_DAILY_REMINDER;

public class MainActivity extends AppCompatActivity implements ProgressBarDisplay {
    private ProgressBar progressBar;
    public static final String ACTION_DOWNLOAD_STATUS = "download_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        setTitle(R.string.title_apps);

        final ArrayList<Movie> listTodayMovie = new ArrayList<>();
        final ReminderReceiver mReminderReceiver = new ReminderReceiver();
        final ReminderPreference mReminderPreference = new ReminderPreference(this);

        progressBar = findViewById(R.id.progress_bar);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //startJob();
        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_movie);
        }
        /*
        if(mReminderPreference.getDailyRemainder()) {
            //mReminderReceiver.setDailyAlarm(MainActivity.this, ID_DAILY_REMINDER, DAILY_MESSAGE);
        } else {
            mReminderReceiver.cancelAlarm(MainActivity.this, ID_DAILY_REMINDER);
        }

        if(mReminderPreference.getReleaseRemainder()) {
            // download and save in view model
            // pass message to set daily alarm
            // looping maks 3 buat notif dengan judul film
            TodayMovieViewModel todayMovieViewModel = ViewModelProviders.of(this).get(TodayMovieViewModel.class);
            todayMovieViewModel.setTodayMovies();
            todayMovieViewModel.getTodayMovies().observe(this, new Observer<ArrayList<Movie>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Movie> movies) {
                    listTodayMovie.clear();
                    listTodayMovie.addAll(movies);
                    int id = ReminderReceiver.ID_RELEASE_REMINDER;
                    for(int i = 0; i < 3; i++) {
                        Log.d("tes overview","---------------------Movie overview : "+listTodayMovie.get(i).getName());
                        mReminderReceiver.setDailyAlarm(MainActivity.this, id, listTodayMovie.get(i).getName());
                        id++;
                    }
                }
            });

        } else {
            mReminderReceiver.cancelAlarm(MainActivity.this, ReminderReceiver.ID_RELEASE_REMINDER);
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        final BottomNavigationView bottomNavView = findViewById(R.id.nav_view);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search_menu)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(MainActivity.this, "searching for " + query, Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    int id = getSelectedMenuItem(bottomNavView);
                    if (id == R.id.navigation_movie || id == R.id.navigation_fav_movie) {
                        Fragment fragmentSearchMovie = new SearchMovieFragment();
                        bundle.putString(SearchMovieFragment.SEARCH_MOVIE, query);
                        fragmentSearchMovie.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_layout, fragmentSearchMovie, fragmentSearchMovie.getClass().getSimpleName())
                                .commit();
                    } else if (id == R.id.navigation_show || id == R.id.navigation_fav_show ) {
                        Fragment fragmentSearchShow = new SearchShowFragment();
                        bundle.putString(SearchMovieFragment.SEARCH_MOVIE, query);
                        fragmentSearchShow.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_layout, fragmentSearchShow, fragmentSearchShow.getClass().getSimpleName())
                                .commit();
                    }
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_settings:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                break;
            case R.id.action_change_reminder:
                Intent reminderIntent = new Intent(MainActivity.this, ReminderActivity.class);
                startActivity(reminderIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragmentMovie;
            Fragment fragmentShow;

            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    fragmentMovie = new MovieFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragmentMovie, fragmentMovie.getClass().getSimpleName())
                            .commit();

                    return true;
                case R.id.navigation_show:
                    fragmentShow = new ShowFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragmentShow, fragmentShow.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_fav_movie:
                    fragmentMovie = new FavMovieFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragmentMovie, fragmentMovie.getClass().getSimpleName())
                            .commit();

                    return true;
                case R.id.navigation_fav_show:
                    fragmentShow = new FavTvShowFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragmentShow, fragmentShow.getClass().getSimpleName())
                            .commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private int getSelectedMenuItem(BottomNavigationView mBottomNavigationView) {
        Menu menu = mBottomNavigationView.getMenu();
        int id = 0;
        for (int i = 0; i < mBottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.isChecked()) {
                id = menuItem.getItemId();
                i = mBottomNavigationView.getMenu().size();
            }
        }
        return id;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
