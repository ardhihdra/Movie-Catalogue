package com.example.myfilmandtvlist.databaseMovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myfilmandtvlist.favWidget.FavoriteFilmWidget;
import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.favMovie.FavMovieViewModel;
import com.example.myfilmandtvlist.movie.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.myfilmandtvlist.databaseMovie.DatabaseContract.FavColumns.POSTER;
import static com.example.myfilmandtvlist.databaseMovie.DatabaseContract.FavColumns.RATING;
import static com.example.myfilmandtvlist.databaseMovie.DatabaseContract.FavColumns.TITLE;
import static com.example.myfilmandtvlist.databaseMovie.DatabaseContract.TABLE_FAV;

public class FavMovieHelper {
    // using singleton pattern

    private static final String DATABASE_TABLE = TABLE_FAV;
    private static DatabaseHelper sDatabaseHelper;
    private static FavMovieHelper INSTANCE;

    private static SQLiteDatabase database;

    private FavMovieHelper(Context context) {
        sDatabaseHelper = new DatabaseHelper(context);
    }

    public static FavMovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavMovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    //buka tutup database
    public void open() throws SQLException {
            database = sDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        sDatabaseHelper.close();

        if (database.isOpen()) database.close();
    }


    //CRUD
    public ArrayList<Movie> getAllFavMovies() { //ArrayList<Movie>
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE,null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setName(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setRating(Double.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(RATING))));
                movie.setPhotoURL(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));

                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertFavMovie(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(_ID, movie.getId());
        args.put(TITLE, movie.getName());
        args.put(RATING, movie.getRating());
        args.put(POSTER, movie.getPhotoURL());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteFavMovie(int id) {
        return database.delete(TABLE_FAV, _ID + " = '" + id + "'", null);
    }

    public int deleteAllFavMovie() {
        return database.delete(TABLE_FAV, null, null);
    }


    public void favSelectedMovie(Movie movie, FragmentActivity c, ImageView v, FavMovieViewModel fav) {
        //long result = 1;
        if(movie.isFavorite()) {
            long result = this.deleteFavMovie(movie.getId());
            if (result > 0) {
                movie.setFavorite(false);
                fav.setFavMovies(this.getAllFavMovies());
                v.setImageResource(R.drawable.ic_favorite_black_24dp);

                FavoriteFilmWidget.sendRefreshBroadcast(c);
                Toast.makeText(c, String.format("%s Dihapus dari Favorit",movie.getName()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(c, "Gagal dihapus dari favorit", Toast.LENGTH_SHORT).show();
            }
        } else {
            long result = this.insertFavMovie(movie);
            if (result > 0) {
                movie.setFavorite(true);
                fav.setFavMovies(this.getAllFavMovies());
                v.setImageResource(R.drawable.ic_favorite_red_24dp);

                FavoriteFilmWidget.sendRefreshBroadcast(c);
                Toast.makeText(c, String.format("%s Ditambahkan ke Favorit",movie.getName()), Toast.LENGTH_SHORT).show();
                //Toast.makeText(c, String.format("%s favorit",listFavMovie.size()), Toast.LENGTH_SHORT).show();
            } else {
                this.deleteFavMovie(movie.getId());
                Toast.makeText(c, "Gagal memfavoritkan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void redFavoriteMovie(ArrayList<Movie> a, ArrayList<Movie> listFavorite) {
        boolean found = false;
        int j = 0;
        for(int i = 0; i < a.size(); i++) { //looping around a
            while (j < listFavorite.size() && !found) { // looping around listFavorite
                if (a.get(i).getId() == listFavorite.get(j).getId()) {
                    a.get(i).setFavorite(true);
                    found = true;
                }
                j++;
            }
            found = false;
            j = 0;
        }
    }
}
