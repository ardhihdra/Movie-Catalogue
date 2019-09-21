package com.example.myfilmandtvlist.favShow;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.myfilmandtvlist.tvShow.TvShow;

import java.util.ArrayList;

public class FavTvShowViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TvShow>> listFavTvShow = new MutableLiveData<>();

    public LiveData<ArrayList<TvShow>> getFavTvShow() { return listFavTvShow; }

    public final void setFavTvShow(ArrayList<TvShow> favDatabase) {
        listFavTvShow.postValue(favDatabase);
    }
}
