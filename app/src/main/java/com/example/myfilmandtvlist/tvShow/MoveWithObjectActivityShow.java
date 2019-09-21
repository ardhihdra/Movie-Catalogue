package com.example.myfilmandtvlist.tvShow;

import android.content.Intent;
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

public class MoveWithObjectActivityShow extends AppCompatActivity {
    TextView obj_name,obj_description,obj_rating,obj_release;
    ImageView obj_photo;

    public static final String EXTRA_SHOW= "YAALLOH";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail);

        TvShow show = getIntent().getParcelableExtra(EXTRA_SHOW);

        obj_name = findViewById(R.id.obj_name);
        obj_description = findViewById(R.id.obj_description);
        obj_rating = findViewById(R.id.obj_rating);
        obj_release = findViewById(R.id.obj_release);
        obj_photo = findViewById(R.id.obj_image);

        obj_name.setText(show.getName());
        obj_rating.setText(String.valueOf(show.getRating()));
        obj_release.setText(show.getRelease());
        obj_description.setText(show.getDescription());
        Glide.with(MoveWithObjectActivityShow.this)
                .load(show.getPhotoURL())
                .into(obj_photo);
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
