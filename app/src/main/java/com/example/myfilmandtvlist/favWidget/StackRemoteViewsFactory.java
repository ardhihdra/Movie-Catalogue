package com.example.myfilmandtvlist.favWidget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.databaseMovie.FavMovieHelper;
import com.example.myfilmandtvlist.databaseTvShow.FavTvShowHelper;
import com.example.myfilmandtvlist.movie.Movie;
import com.example.myfilmandtvlist.tvShow.TvShow;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.System.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Bitmap> mWidgetItems = new ArrayList<>();
    private final Context mContext;
    private FavMovieHelper mFavMovieHelper;
    private FavTvShowHelper mFavTvShowHelper;
    private Cursor cursor;

    public StackRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        //add image here cu
        mFavTvShowHelper = FavTvShowHelper.getInstance(mContext);
        mFavMovieHelper = FavMovieHelper.getInstance(mContext);
        mWidgetItems.clear();
        mWidgetItems = addMovieToWidget(mContext, mWidgetItems, mFavMovieHelper);

        mWidgetItems = addShowToWidget(mContext, mWidgetItems, mFavTvShowHelper);

        if (cursor != null){
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        // querying ke database
        cursor = mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));

        Bundle extras = new Bundle();
        extras.putInt(FavoriteFilmWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    public List<Bitmap> addMovieToWidget(Context context, List<Bitmap> image, FavMovieHelper favMovieHelper) {
        String photoUrl;
        favMovieHelper.open();
        ArrayList<Movie> favMovie = favMovieHelper.getAllFavMovies();
        for (int i = 0; i < favMovie.size(); i++) {
            try {
                photoUrl = favMovie.get(i).getPhotoURL();
                Bitmap pict = Glide.with(mContext.getApplicationContext())
                        .asBitmap()
                        .load(photoUrl)
                        .submit()
                        .get();
                image.add(pict);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //favMovieHelper.close();

        return image;
    }

    public List<Bitmap> addShowToWidget(Context context, List<Bitmap> images, FavTvShowHelper favTvShowHelper) {
        mFavTvShowHelper.open();
        String photoUrl;
        ArrayList<TvShow> favShow = mFavTvShowHelper.getAllFavTvShow();
        for (int i = 0; i < favShow.size(); i++) {
            try {
                photoUrl = favShow.get(i).getPhotoURL();
                mWidgetItems.add(Glide.with(mContext.getApplicationContext())
                        .asBitmap()
                        .load(photoUrl)
                        .submit()
                        .get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //mFavTvShowHelper.close();

        return images;
    }
}
