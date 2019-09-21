package com.example.myfilmandtvlist.tvShow;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.myfilmandtvlist.UrlApi;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ShowViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TvShow>> listShows = new MutableLiveData<>();

    public LiveData<ArrayList<TvShow>> getShows() {
        return listShows;
    }

    public void setShows() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> listItems = new ArrayList<>();
        String url = UrlApi.discoverShow;
        //AsyncHttpResponseHandler
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject showObject = list.getJSONObject(i);
                        TvShow show = new TvShow(showObject);
                        //Log.d("tes overview","---------------------Movie overview : "+show.getName());
                        listItems.add(show);
                    }
                    listShows.postValue(listItems);

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
