package com.example.myfilmandtvlist.favShow;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfilmandtvlist.ProgressBarDisplay;
import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.databaseTvShow.FavTvShowHelper;
import com.example.myfilmandtvlist.tvShow.TvShow;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavTvShowFragment extends Fragment {
    private ArrayList<TvShow> listFavTvShow = new ArrayList<>();
    private FavTvShowAdapter adapter;
    private FavTvShowHelper mFavTvShowHelper;


    public FavTvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity c = (AppCompatActivity) getActivity();
        if (c.getSupportActionBar() != null) {
            c.getSupportActionBar().setTitle("Your Favorite Tv Show");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_fav_tv_show, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentActivity c = getActivity();
        final RecyclerView rvCategory;
        mFavTvShowHelper = FavTvShowHelper.getInstance(c);
        mFavTvShowHelper.open();

        showProgress();
        FavTvShowViewModel tvShowViewModel = ViewModelProviders.of(this).get(FavTvShowViewModel.class);
        tvShowViewModel.setFavTvShow(mFavTvShowHelper.getAllFavTvShow());
        tvShowViewModel.getFavTvShow().observe(this,new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> tvShow) {
                if (tvShow != null) {
                    adapter.setFavData(tvShow);
                    hideProgress();
                }
            }
        });

        adapter = new FavTvShowAdapter(c);
        adapter.setListFavTvShow(listFavTvShow);

        rvCategory = view.findViewById(R.id.rv_favtvshow_category);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new GridLayoutManager(c, 3));
        rvCategory.setAdapter(adapter);
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
}
