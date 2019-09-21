package com.example.myfilmandtvlist.favMovie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.myfilmandtvlist.movie.Movie;

import java.util.ArrayList;

public class FavMovieViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Movie>> listFavMovies = new MutableLiveData<>();

    public LiveData<ArrayList<Movie>> getFavMovies() { return listFavMovies; }

    public final void setFavMovies(ArrayList<Movie> favDatabase) {
        listFavMovies.postValue(favDatabase);
    }


}
