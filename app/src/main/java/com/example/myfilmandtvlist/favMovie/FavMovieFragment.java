package com.example.myfilmandtvlist.favMovie;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfilmandtvlist.ProgressBarDisplay;
import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.databaseMovie.FavMovieHelper;
import com.example.myfilmandtvlist.movie.Movie;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavMovieFragment extends Fragment {
    private ArrayList<Movie> listFavMovie = new ArrayList<>();
    private FavMovieAdapter adapter;
    private FavMovieHelper mFavMovieHelper;
    public FavMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity c = (AppCompatActivity) getActivity();
        if (c.getSupportActionBar() != null) {
            c.getSupportActionBar().setTitle("Your Favorite Movie");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_fav_movie, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView rvCategory;
        final FragmentActivity c = getActivity();
        mFavMovieHelper = FavMovieHelper.getInstance(c);
        mFavMovieHelper.open();

        showProgress();
        FavMovieViewModel movieViewModel = ViewModelProviders.of(this).get(FavMovieViewModel.class);
        movieViewModel.setFavMovies(mFavMovieHelper.getAllFavMovies());
        movieViewModel.getFavMovies().observe(this,new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movie) {
                if (movie != null) {
                    adapter.setFavData(movie);
                    hideProgress();
                }
            }
        });

        adapter = new FavMovieAdapter(c);
        adapter.setListFavMovie(listFavMovie);

        rvCategory = view.findViewById(R.id.rv_favmovie_category);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new GridLayoutManager(c, 3));
        rvCategory.setAdapter(adapter);

    }

    protected void showProgress() {
        if (getActivity() instanceof ProgressBarDisplay) {
            ((ProgressBarDisplay) getActivity()).showProgress();
        }
    }

    protected void hideProgress() {
        if (getActivity() instanceof ProgressBarDisplay) {
            ((ProgressBarDisplay) getActivity()).hideProgress();
        }
    }


}
