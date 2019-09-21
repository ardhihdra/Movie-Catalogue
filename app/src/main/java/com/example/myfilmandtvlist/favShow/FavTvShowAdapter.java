package com.example.myfilmandtvlist.favShow;

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
import com.example.myfilmandtvlist.tvShow.TvShow;

import java.util.ArrayList;

public class FavTvShowAdapter extends RecyclerView.Adapter<FavTvShowAdapter.ShowViewHolder> {
    private ArrayList<TvShow> listFavTvShow;
    private Context context;

    public void setFavData(ArrayList<TvShow> items) {
        listFavTvShow.clear();
        listFavTvShow.addAll(items);
        notifyDataSetChanged();
    }

    public FavTvShowAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<TvShow> getListFavTvShow() {
        return listFavTvShow;
    }

    public void setListFavTvShow(ArrayList<TvShow> listShow) {
        this.listFavTvShow = listShow;
    }

    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_favtvshow, viewGroup, false);
        return new ShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavTvShowAdapter.ShowViewHolder holder, int i) {
        holder.tvTitle.setText(listFavTvShow.get(i).getName());
        Glide.with(context)
                .load(listFavTvShow.get(i).getPhotoURL())
                .override(R.dimen.list_poster_width,R.dimen.list_poster_height)
                .into(holder.tvPhoto);
    }

    @Override
    public int getItemCount() { return getListFavTvShow().size(); }


    class ShowViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final ImageView tvPhoto;
        final CardView cvNote;

        ShowViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.txt_name);
            tvPhoto = itemView.findViewById(R.id.img_photo);
            cvNote = itemView.findViewById(R.id.img_photo_rounded);
        }
    }
}
