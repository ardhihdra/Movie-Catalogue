package com.example.myfilmandtvlist.todayFilm.ui.todaymovie;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfilmandtvlist.R;

public class TodayMovieFragment extends Fragment {

    private TodayMovieViewModel mViewModel;

    public static TodayMovieFragment newInstance() {
        return new TodayMovieFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.today_movie_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TodayMovieViewModel.class);
        // TODO: Use the ViewModel
    }

}
