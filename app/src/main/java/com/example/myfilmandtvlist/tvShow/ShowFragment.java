package com.example.myfilmandtvlist.tvShow;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myfilmandtvlist.ItemClickSupport;
import com.example.myfilmandtvlist.ProgressBarDisplay;
import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.databaseTvShow.FavTvShowHelper;
import com.example.myfilmandtvlist.favShow.FavTvShowViewModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowFragment extends Fragment {
    private RecyclerView rvCategory;
    private ArrayList<TvShow> listShows = new ArrayList<>();
    private ArrayList<TvShow> listFavTvShow = new ArrayList<>();
    private ShowAdapter adapter;

    private FavTvShowHelper mFavTvShowHelper;

    public ShowFragment() {
        // Required empty public constructor
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity c = (AppCompatActivity) getActivity();
        if (c.getSupportActionBar() != null) {
            c.getSupportActionBar().setTitle("Discover Tv Show");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_show, container, false);
        final FragmentActivity c = getActivity();
        mFavTvShowHelper = FavTvShowHelper.getInstance(c);
        mFavTvShowHelper.open();

        showProgress();
        ShowViewModel showViewModel = ViewModelProviders.of(this).get(ShowViewModel.class);
        showViewModel.setShows();
        final FavTvShowViewModel favTvShowViewModel = ViewModelProviders.of(this).get(FavTvShowViewModel.class);
        favTvShowViewModel.setFavTvShow(mFavTvShowHelper.getAllFavTvShow());
        favTvShowViewModel.getFavTvShow().observe(this, new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(@Nullable ArrayList<TvShow> show) {
                listFavTvShow.clear();
                listFavTvShow.addAll(show);
            }
        });
        showViewModel.getShows().observe(this,new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> show) {
                if (show != null) {
                    redFavoriteTvShow(show, listFavTvShow);
                    adapter.setData(show);
                    hideProgress();
                }
            }
        });

        adapter = new ShowAdapter(c);
        //mFavTvShowHelper.deleteAllFavTvShow();
        adapter.setListShow(listShows);

        rvCategory = view.findViewById(R.id.rv_show_category);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(c));
        rvCategory.setAdapter(adapter);

        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedShow(listShows.get(position), c);
            }
        });

        adapter.setOnItemClickCallback(new ShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow show, View a) {
                ImageView v = (ImageView) a;
                favSelectedTvShow(show, c, v, favTvShowViewModel);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    protected void showProgress() {
        if (getActivity() instanceof ProgressBarDisplay) {
            ((ProgressBarDisplay) getActivity()).showProgress();
        }
    }

    protected void hideProgress() {
        if (getActivity() instanceof ProgressBarDisplay) {
            ((ProgressBarDisplay) getActivity()).hideProgress();
        }
    }

    private void showSelectedShow(TvShow show, FragmentActivity c){
        Intent moveWithObjectIntent = new Intent(c, MoveWithObjectActivityShow.class);
        moveWithObjectIntent.putExtra(MoveWithObjectActivityShow.EXTRA_SHOW, show);
        startActivity(moveWithObjectIntent);
    }

    private void favSelectedTvShow(TvShow show, FragmentActivity c, ImageView v, FavTvShowViewModel fav) {
        //long result = 1;
        if(show.isFavorite()) {
            long result = mFavTvShowHelper.deleteFavTvShow(show.getId());
            if (result > 0) {
                show.setFavorite(false);
                fav.setFavTvShow(mFavTvShowHelper.getAllFavTvShow());
                v.setImageResource(R.drawable.ic_favorite_black_24dp);
                //Toast.makeText(c, String.format("%s Dihapus dari Favorit",show.getName()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(c, "Gagal dihapus dari favorit", Toast.LENGTH_SHORT).show();
            }
        } else {
            long result = mFavTvShowHelper.insertFavTvShow(show);
            if (result > 0) {
                show.setFavorite(true);
                fav.setFavTvShow(mFavTvShowHelper.getAllFavTvShow());
                v.setImageResource(R.drawable.ic_favorite_red_24dp);
                Toast.makeText(c, String.format("%s Ditambahkan ke Favorit",show.getName()), Toast.LENGTH_SHORT).show();
                //Toast.makeText(c, String.format("%s favorit",listFavTvShow.size()), Toast.LENGTH_SHORT).show();
            } else {
                mFavTvShowHelper.deleteFavTvShow(show.getId());
                Toast.makeText(c, "Gagal memfavoritkan", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void redFavoriteTvShow(ArrayList<TvShow> a, ArrayList<TvShow> listFavorite) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFavTvShowHelper.close();
    }
}
