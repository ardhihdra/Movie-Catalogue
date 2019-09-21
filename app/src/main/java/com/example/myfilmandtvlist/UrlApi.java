package com.example.myfilmandtvlist;

public class UrlApi {
    public static final String API_KEY = "3715f2e96443977429224543ef59b7b9";
    public static final String popularMovie = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY + "&language=en-US&page=1";
    public static final String discoverMovie = "https://api.themoviedb.org/3/discover/movie?api_key="+ API_KEY +"&language=en-US";
    public static final String genreList = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY + "&language=en-US";
    public static final String discoverShow = "https://api.themoviedb.org/3/discover/tv?api_key="+ API_KEY +"&language=en-US";

    public String findTodayMovie(String date) {
        return "https://api.themoviedb.org/3/discover/movie?api_key="+API_KEY+"&primary_release_date.gte="+date+"&primary_release_date.lte="+date;
    }

    public String findMovie(String movie_name) {
        return "https://api.themoviedb.org/3/search/movie?api_key="+ API_KEY +"&language=en-US&query="+movie_name;
    }
    public String findShow(String show_name) {
        return "https://api.themoviedb.org/3/search/tv?api_key="+ API_KEY +"&language=en-US&query="+show_name;
    }

}
