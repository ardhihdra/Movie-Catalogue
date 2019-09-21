package com.example.myfilmandtvlist.movie;

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

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CatagoryViewHolder> {
    private Context context;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    private ArrayList<Movie> listMovie;

    public void setData(ArrayList<Movie> items) {
        listMovie.clear();
        listMovie.addAll(items);
        notifyDataSetChanged();
    }

    public MovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Movie> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList listMovie) {
        this.listMovie = listMovie;
    }

    @NonNull
    @Override
    public CatagoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_film, viewGroup, false);
        return new CatagoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final CatagoryViewHolder holder, int i) {
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
                onItemClickCallback.onItemClicked(listMovie.get(holder.getAdapterPosition()), v);

            }

        });

    }

    @Override
    public int getItemCount() {
        return getListMovie().size();
    }


    public class CatagoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvrating;
        TextView tvgenre;
        ImageView imgPhoto, imgFav;


        public CatagoryViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.txt_name);
            tvrating = itemView.findViewById(R.id.txt_rating);
            tvgenre = itemView.findViewById(R.id.txt_genre);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            imgFav = itemView.findViewById(R.id.img_favorite);
            //progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie movie, View v);
    }
}
