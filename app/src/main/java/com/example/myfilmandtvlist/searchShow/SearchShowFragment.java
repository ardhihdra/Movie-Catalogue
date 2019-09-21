package com.example.myfilmandtvlist.searchShow;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myfilmandtvlist.ItemClickSupport;
import com.example.myfilmandtvlist.ProgressBarDisplay;
import com.example.myfilmandtvlist.R;
import com.example.myfilmandtvlist.databaseTvShow.FavTvShowHelper;
import com.example.myfilmandtvlist.favShow.FavTvShowViewModel;
import com.example.myfilmandtvlist.tvShow.MoveWithObjectActivityShow;
import com.example.myfilmandtvlist.tvShow.TvShow;

import java.util.ArrayList;

public class SearchShowFragment extends Fragment {
    private RecyclerView rvCategory;
    private ArrayList<TvShow> listSearchedTvShows = new ArrayList<>();
    private ArrayList<TvShow> listFavTvShow = new ArrayList<>();
    private SearchShowAdapter adapter;
    private FavTvShowHelper favTvShowHelper;
    //String movie_name;


    public static String SEARCH_SHOW = "show_name";

    private SearchShowViewModel mViewModel;

    public static SearchShowFragment newInstance() {
        return new SearchShowFragment();
    }

    public SearchShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity c = (AppCompatActivity) getActivity();
        if (c.getSupportActionBar() != null) {
            c.getSupportActionBar().setTitle("Search Tv Show");
        }

        if(getArguments() != null) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentActivity c = getActivity();

        adapter = new SearchShowAdapter(c);
        favTvShowHelper = FavTvShowHelper.getInstance(c);
        favTvShowHelper.open();
        String show_name = getArguments().getString(SEARCH_SHOW);
        //movie_name = getArguments().getString(SEARCH_MOVIE);
        showProgress();

        SearchShowViewModel searchTvShowViewModel = ViewModelProviders.of(this).get(SearchShowViewModel.class);
        searchTvShowViewModel.setTvShow(show_name);
        final FavTvShowViewModel favTvShowViewModel = ViewModelProviders.of(this).get(FavTvShowViewModel.class);
        favTvShowViewModel.setFavTvShow(favTvShowHelper.getAllFavTvShow());
        favTvShowViewModel.getFavTvShow().observe(this, new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(@Nullable ArrayList<TvShow> show) {
                listFavTvShow.clear();
                listFavTvShow.addAll(show);
            }
        });
        searchTvShowViewModel.getTvShow().observe(this,new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> show) {
                if (show != null) {
                    favTvShowHelper.redFavoriteTvShow(show, listFavTvShow);
                    adapter.setData(show);
                    hideProgress();
                }
            }
        });

        adapter.setListTvShow(listSearchedTvShows);

        rvCategory = view.findViewById(R.id.rv_show_category);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(c));
        rvCategory.setAdapter(adapter);

        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedTvShow(listSearchedTvShows.get(position), c);
            }
        });

        adapter.setOnItemClickCallback(new SearchShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow show, View a) {
                ImageView v = (ImageView) a;
                favTvShowHelper.favSelectedTvShow(show, c, v, favTvShowViewModel);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final FragmentActivity c = getActivity();

        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) c.getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search_menu)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(c.getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(c, R.string.search_for + query, Toast.LENGTH_SHORT).show();

                    Fragment fragmentSearchTvShow = new SearchShowFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(SEARCH_SHOW, query);
                    fragmentSearchTvShow.setArguments(bundle);
                    c.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragmentSearchTvShow, fragmentSearchTvShow.getClass().getSimpleName())
                            .commit();
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    private void showSelectedTvShow(TvShow show, FragmentActivity c){
        Intent moveWithObjectIntent = new Intent(c, MoveWithObjectActivityShow.class);
        moveWithObjectIntent.putExtra(MoveWithObjectActivityShow.EXTRA_SHOW, show);
        startActivity(moveWithObjectIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_settings:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        favTvShowHelper.close();
    }

}
