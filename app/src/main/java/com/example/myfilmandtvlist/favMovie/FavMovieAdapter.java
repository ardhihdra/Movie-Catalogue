package com.example.myfilmandtvlist.favMovie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.movie.Movie;

import java.util.ArrayList;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.MovieViewHolder> {
    private ArrayList<Movie> listFavMovie;
    private Context context;

    public void setFavData(ArrayList<Movie> items) {
        listFavMovie.clear();
        listFavMovie.addAll(items);
        notifyDataSetChanged();
    }

    public FavMovieAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Movie> getListFavMovie() {
        return listFavMovie;
    }

    public void setListFavMovie(ArrayList<Movie> listMovie) {
        this.listFavMovie = listMovie;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_favfilm, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavMovieAdapter.MovieViewHolder holder, int i) {
        holder.tvTitle.setText(listFavMovie.get(i).getName());
        Glide.with(context)
                .load(listFavMovie.get(i).getPhotoURL())
                .override(R.dimen.list_poster_width,R.dimen.list_poster_height)
                .into(holder.tvPhoto);
    }

    @Override
    public int getItemCount() { return getListFavMovie().size(); }


    // add, update and delete
    public void addItem(Movie movie) {
        this.listFavMovie.add(movie);
        notifyItemInserted(listFavMovie.size() - 1);
    }
    public void updateItem(int position, Movie movie) {
        this.listFavMovie.set(position, movie);
        notifyItemChanged(position, movie);
    }
    public void removeItem(int position) {
        this.listFavMovie.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,listFavMovie.size());
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final ImageView tvPhoto;
        final CardView cvNote;

        MovieViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.txt_name);
            tvPhoto = itemView.findViewById(R.id.img_photo);
            cvNote = itemView.findViewById(R.id.img_photo_rounded);
        }
    }
}
