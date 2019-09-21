package com.example.myfilmandtvlist.searchMovie;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class SearchMovieAdapter extends RecyclerView.Adapter<SearchMovieAdapter.SearchViewHolder> {
    private Context context;

    private SearchMovieAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(SearchMovieAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    private ArrayList<Movie> listSearchMovie;

    public void setData(ArrayList<Movie> items) {
        listSearchMovie.clear();
        listSearchMovie.addAll(items);
        notifyDataSetChanged();
    }

    public SearchMovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Movie> getListMovie() {
        return listSearchMovie;
    }

    public void setListMovie(ArrayList listMovie) {
        this.listSearchMovie = listMovie;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_film, viewGroup, false);
        return new SearchViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder holder, int i) {
        holder.tvName.setText(getListMovie().get(i).getName());
        holder.tvrating.setText(String.valueOf(getListMovie().get(i).getRating()));
        Glide.with(context)
                .load(getListMovie().get(i).getPhotoURL())
                .override(R.dimen.list_poster_width,R.dimen.list_poster_height)
                .into(holder.imgPhoto);
        holder.tvgenre.setText(String.valueOf(getListMovie().get(i).getVoter()));
        if(getListMovie().get(i).isFavorite()) {
            holder.imgFav.setImageResource(R.drawable.ic_favorite_red_24dp);
        } else {
            holder.imgFav.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        holder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listSearchMovie.get(holder.getAdapterPosition()), v);

            }

        });
    }

    @Override
    public int getItemCount() {
        return getListMovie().size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvrating;
        TextView tvgenre;
        ImageView imgPhoto, imgFav;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.txt_name);
            tvrating = itemView.findViewById(R.id.txt_rating);
            tvgenre = itemView.findViewById(R.id.txt_genre);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            imgFav = itemView.findViewById(R.id.img_favorite);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie movie, View v);
    }
}
