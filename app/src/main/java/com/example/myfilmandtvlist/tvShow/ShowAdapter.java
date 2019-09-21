package com.example.myfilmandtvlist.tvShow;

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

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.CatagoryViewHolder> {
    private Context context;

    private ArrayList<TvShow> listShow;
    private ShowAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<TvShow> items) {
        listShow.clear();
        listShow.addAll(items);
        notifyDataSetChanged();
    }

    public ShowAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<TvShow> getListShow() {
        return listShow;
    }

    public void setListShow(ArrayList listShow) {
        this.listShow = listShow;
    }

    @NonNull
    @Override
    public CatagoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_film, viewGroup, false);
        return new CatagoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final CatagoryViewHolder holder, int i) {
        holder.tvName.setText(getListShow().get(i).getName());
        holder.tvRating.setText(String.valueOf(getListShow().get(i).getRating()));
        Glide.with(context)
                .load(getListShow().get(i).getPhotoURL())
                .override(R.dimen.list_poster_width,R.dimen.list_poster_height)
                .into(holder.imgPhoto);
        holder.tvGenre.setText(String.valueOf(getListShow().get(i).getVoter()));
        if(getListShow().get(i).isFavorite()) {
            holder.imgFav.setImageResource(R.drawable.ic_favorite_red_24dp);
        } else {
            holder.imgFav.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        holder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listShow.get(holder.getAdapterPosition()), v);

            }

        });
    }

    @Override
    public int getItemCount() {
        return getListShow().size();
    }


    public class CatagoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvRating;
        TextView tvGenre;
        ImageView imgPhoto, imgFav;

        public CatagoryViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.txt_name);
            tvRating = itemView.findViewById(R.id.txt_rating);
            tvGenre = itemView.findViewById(R.id.txt_genre);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            imgFav = itemView.findViewById(R.id.img_favorite);
            //progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShow show, View v);
    }

}
