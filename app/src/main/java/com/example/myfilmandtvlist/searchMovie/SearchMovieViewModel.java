package com.example.myfilmandtvlist.searchMovie;

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

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchMovieViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private UrlApi mUrlApi = new UrlApi();
    private MutableLiveData<ArrayList<Movie>> listSearchedMovies = new MutableLiveData<>();

    public LiveData<ArrayList<Movie>> getMovies() {
        return listSearchedMovies;
    }

    public void setMovies(String movie_name) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        String url = mUrlApi.findMovie(movie_name);

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
                        //Log.d("tes overview","---------------------Movie overview : "+movie.getVoter());
                        listItems.add(movie);
                    }
                    listSearchedMovies.postValue(listItems);

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
