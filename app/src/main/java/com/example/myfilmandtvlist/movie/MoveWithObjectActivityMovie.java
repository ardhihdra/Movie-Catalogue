package com.example.myfilmandtvlist.movie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myfilmandtvlist.R;

public class MoveWithObjectActivityMovie extends AppCompatActivity {
    TextView obj_name,obj_description,obj_rating,obj_release;
    ImageView obj_photo;

    public static final String EXTRA_MOVIE = "Ardhi";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        obj_name = findViewById(R.id.obj_name);
        obj_description = findViewById(R.id.obj_description);
        obj_rating = findViewById(R.id.obj_rating);
        obj_release = findViewById(R.id.obj_release);
        obj_photo = findViewById(R.id.obj_image);

        obj_name.setText(movie.getName());
        obj_rating.setText(String.valueOf(movie.getRating()));
        obj_release.setText(movie.getRelease());
        obj_description.setText(movie.getDescription());
        Glide.with(MoveWithObjectActivityMovie.this)
                .load(movie.getPhotoURL())
                .into(obj_photo);
        //Log.d("cek", "tes rating " + movie.getRating());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }


}
