package com.example.myfilmandtvlist.favWidget;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.databaseMovie.FavMovieHelper;
import com.example.myfilmandtvlist.databaseTvShow.FavTvShowHelper;

import java.util.ArrayList;
import java.util.List;


public class UpdateWidgetService extends JobService {
    Context mContext;
    private FavMovieHelper mFavMovieHelper;
    private FavTvShowHelper mFavTvShowHelper;
    private final List<Bitmap> mWidgetItems = new ArrayList<>();

    public UpdateWidgetService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget_item);
        ComponentName theWidget = new ComponentName(this, FavoriteFilmWidget.class);
        //view.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));

        manager.updateAppWidget(theWidget, view);
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
