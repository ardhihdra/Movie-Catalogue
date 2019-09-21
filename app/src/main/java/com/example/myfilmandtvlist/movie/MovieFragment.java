package com.example.myfilmandtvlist.movie;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myfilmandtvlist.ItemClickSupport;
import com.example.myfilmandtvlist.ProgressBarDisplay;
import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.databaseMovie.FavMovieHelper;
import com.example.myfilmandtvlist.favMovie.FavMovieViewModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {
    private RecyclerView rvCategory;
    private ArrayList<Movie> listMovie = new ArrayList<>();
    private ArrayList<Movie> listFavMovie = new ArrayList<>();
    private MovieAdapter adapter;
    private FavMovieHelper mFavMovieHelper;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity c = (AppCompatActivity) getActivity();
        if (c.getSupportActionBar() != null) {
            c.getSupportActionBar().setTitle("Discover Movie");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentActivity c = getActivity();

        adapter = new MovieAdapter(c);
        mFavMovieHelper = FavMovieHelper.getInstance(c);
        mFavMovieHelper.open();

        showProgress();

        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.setMovies();
        final FavMovieViewModel favMovieViewModel = ViewModelProviders.of(this).get(FavMovieViewModel.class);
        favMovieViewModel.setFavMovies(mFavMovieHelper.getAllFavMovies());
        favMovieViewModel.getFavMovies().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Movie> movies) {
                listFavMovie.clear();
                listFavMovie.addAll(movies);
                //redFavoriteMovie(listFavMovie, listFavMovie);
                //Toast.makeText(c, " jumlah listMovie : " + listMovie.size(), Toast.LENGTH_SHORT );
            }
        });
        movieViewModel.getMovies().observe(this,new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movie) {
                if (movie != null) {
                    mFavMovieHelper.redFavoriteMovie(movie, listFavMovie);
                    adapter.setData(movie);
                    hideProgress();
                }
            }
        });

        //mFavMovieHelper.deleteAllFavMovie(); buat clear data dulu
        adapter.setListMovie(listMovie);

        rvCategory = view.findViewById(R.id.rv_movie_category);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(c));
        rvCategory.setAdapter(adapter);

        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(listMovie.get(position), c);
            }
        });

        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie, View a) {
                ImageView v = (ImageView) a;
                mFavMovieHelper.favSelectedMovie(movie, c, v, favMovieViewModel);
            }
        });
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

    private void showSelectedMovie(Movie movie, FragmentActivity c){
        Intent moveWithObjectIntent = new Intent(c, MoveWithObjectActivityMovie.class);
        moveWithObjectIntent.putExtra(MoveWithObjectActivityMovie.EXTRA_MOVIE, movie);
        startActivity(moveWithObjectIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFavMovieHelper.close();
    }
}


