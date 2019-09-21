package com.example.myfilmandtvlist.todayFilm.ui.todaymovie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.myfilmandtvlist.UrlApi;
import com.example.myfilmandtvlist.movie.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class TodayMovieViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private SimpleDateFormat date = new SimpleDateFormat("YYYY-MM-dd");
    private String currentDate = date.format(new Date());
    private UrlApi mUrlApi = new UrlApi();

    private MutableLiveData<ArrayList<Movie>> listTodayMovies = new MutableLiveData<>();

    public LiveData<ArrayList<Movie>> getTodayMovies() {
        return listTodayMovies;
    }

    public void setTodayMovies() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        String url = mUrlApi.findTodayMovie(currentDate);

        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movieObject = list.getJSONObject(i);
                        Movie movie = new Movie(movieObject);
                        //Log.d("tes overview","---------------------Movie overview : "+movie.getName());
                        listItems.add(movie);
                    }
                    listTodayMovies.postValue(listItems);

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage() + " ----------------- "+ statusCode);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }
}
