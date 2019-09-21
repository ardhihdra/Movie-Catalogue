package com.example.myfilmandtvlist.databaseTvShow;

import android.provider.BaseColumns;

public class DatabaseContract {

    static String TABLE_FAV = "favShow";

    static final class FavColumns implements BaseColumns {

        static String TITLE = "title";
        static String RATING = "rating";
        static String POSTER = "poster";

    }
}
