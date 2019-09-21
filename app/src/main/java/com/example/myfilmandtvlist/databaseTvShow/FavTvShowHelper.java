package com.example.myfilmandtvlist.databaseTvShow;

import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myfilmandtvlist.favWidget.FavoriteFilmWidget;
import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.favShow.FavTvShowViewModel;
import com.example.myfilmandtvlist.tvShow.TvShow;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.myfilmandtvlist.databaseTvShow.DatabaseContract.FavColumns.POSTER;
import static com.example.myfilmandtvlist.databaseTvShow.DatabaseContract.FavColumns.RATING;
import static com.example.myfilmandtvlist.databaseTvShow.DatabaseContract.FavColumns.TITLE;
import static com.example.myfilmandtvlist.databaseTvShow.DatabaseContract.TABLE_FAV;

public class FavTvShowHelper {
    // using singleton pattern


    private static final String DATABASE_TABLE = TABLE_FAV;
    private static DatabaseHelper sDatabaseHelper;
    private static FavTvShowHelper INSTANCE;

    private static SQLiteDatabase database;

    private FavTvShowHelper(Context context) {
        sDatabaseHelper = new DatabaseHelper(context);
    }

    public static FavTvShowHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavTvShowHelper(context);
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
    public ArrayList<TvShow> getAllFavTvShow() { //ArrayList<Movie>
        ArrayList<TvShow> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE,null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        TvShow show;
        if (cursor.getCount() > 0) {
            do {
                show = new TvShow();
                show.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                show.setName(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                show.setRating(Double.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(RATING))));
                show.setPhotoURL(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));

                arrayList.add(show);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertFavTvShow(TvShow show) {
        ContentValues args = new ContentValues();
        args.put(_ID, show.getId());
        args.put(TITLE, show.getName());
        args.put(RATING, show.getRating());
        args.put(POSTER, show.getPhotoURL());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public int deleteFavTvShow(int id) {
        return database.delete(TABLE_FAV, _ID + " = '" + id + "'", null);
    }

    public int deleteAllFavTvShow() {
        return database.delete(TABLE_FAV, null, null);
    }


    public void favSelectedTvShow(TvShow show, FragmentActivity c, ImageView v, FavTvShowViewModel fav) {
        //long result = 1;
        if(show.isFavorite()) {
            long result = this.deleteFavTvShow(show.getId());
            if (result > 0) {
                show.setFavorite(false);
                fav.setFavTvShow(this.getAllFavTvShow());
                v.setImageResource(R.drawable.ic_favorite_black_24dp);

                Intent toastIntent = new Intent(c, FavoriteFilmWidget.class);
                toastIntent.setAction(FavoriteFilmWidget.UPDATE_WIDGET);
                toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, FavoriteFilmWidget.WIDGET_ID);
                //Toast.makeText(c, String.format("%s Dihapus dari Favorit",movie.getName()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(c, "Gagal dihapus dari favorit", Toast.LENGTH_SHORT).show();
            }
        } else {
            long result = this.insertFavTvShow(show);
            if (result > 0) {
                show.setFavorite(true);
                fav.setFavTvShow(this.getAllFavTvShow());
                v.setImageResource(R.drawable.ic_favorite_red_24dp);

                Intent toastIntent = new Intent(c, FavoriteFilmWidget.class);
                toastIntent.setAction(FavoriteFilmWidget.UPDATE_WIDGET);
                toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, FavoriteFilmWidget.WIDGET_ID);
                Toast.makeText(c, String.format("%s Ditambahkan ke Favorit",show.getName()), Toast.LENGTH_SHORT).show();
                //Toast.makeText(c, String.format("%s favorit",listFavMovie.size()), Toast.LENGTH_SHORT).show();
            } else {
                this.deleteFavTvShow(show.getId());
                Toast.makeText(c, "Gagal memfavoritkan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void redFavoriteTvShow(ArrayList<TvShow> a, ArrayList<TvShow> listFavorite) {
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
