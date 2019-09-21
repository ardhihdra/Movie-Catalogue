package com.example.myfilmandtvlist.searchMovie;

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
import com.example.myfilmandtvlist.databaseMovie.FavMovieHelper;
import com.example.myfilmandtvlist.favMovie.FavMovieViewModel;
import com.example.myfilmandtvlist.movie.MoveWithObjectActivityMovie;
import com.example.myfilmandtvlist.movie.Movie;

import java.util.ArrayList;

public class SearchMovieFragment extends Fragment {
    private RecyclerView rvCategory;
    private ArrayList<Movie> listSearchedMovies = new ArrayList<>();
    private ArrayList<Movie> listFavMovie = new ArrayList<>();
    private SearchMovieAdapter adapter;
    private FavMovieHelper favMovieHelper;
    //String movie_name;


    public static String SEARCH_MOVIE = "movie_name";

    private SearchMovieViewModel mViewModel;

    public static SearchMovieFragment newInstance() {
        return new SearchMovieFragment();
    }

    public SearchMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity c = (AppCompatActivity) getActivity();
        if (c.getSupportActionBar() != null) {
            c.getSupportActionBar().setTitle("Search Movie");
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
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentActivity c = getActivity();

        adapter = new SearchMovieAdapter(c);
        favMovieHelper = FavMovieHelper.getInstance(c);
        favMovieHelper.open();
        String movie_name = getArguments().getString(SEARCH_MOVIE);
        //movie_name = getArguments().getString(SEARCH_MOVIE);
        showProgress();

        SearchMovieViewModel searchMovieViewModel = ViewModelProviders.of(this).get(SearchMovieViewModel.class);
        searchMovieViewModel.setMovies(movie_name);
        final FavMovieViewModel favMovieViewModel = ViewModelProviders.of(this).get(FavMovieViewModel.class);
        favMovieViewModel.setFavMovies(favMovieHelper.getAllFavMovies());
        favMovieViewModel.getFavMovies().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Movie> movies) {
                listFavMovie.clear();
                listFavMovie.addAll(movies);
            }
        });
        searchMovieViewModel.getMovies().observe(this,new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movie) {
                if (movie != null) {
                    favMovieHelper.redFavoriteMovie(movie, listFavMovie);
                    adapter.setData(movie);
                    hideProgress();
                }
            }
        });

        //mFavMovieHelper.deleteAllFavMovie();
        adapter.setListMovie(listSearchedMovies);

        rvCategory = view.findViewById(R.id.rv_movie_category);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(c));
        rvCategory.setAdapter(adapter);

        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedMovie(listSearchedMovies.get(position), c);
            }
        });

        adapter.setOnItemClickCallback(new SearchMovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie, View a) {
                ImageView v = (ImageView) a;
                favMovieHelper.favSelectedMovie(movie, c, v, favMovieViewModel);
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

                    Fragment fragmentSearchMovie = new SearchMovieFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(SEARCH_MOVIE, query);
                    fragmentSearchMovie.setArguments(bundle);
                    c.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragmentSearchMovie, fragmentSearchMovie.getClass().getSimpleName())
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

    private void showSelectedMovie(Movie movie, FragmentActivity c){
        Intent moveWithObjectIntent = new Intent(c, MoveWithObjectActivityMovie.class);
        moveWithObjectIntent.putExtra(MoveWithObjectActivityMovie.EXTRA_MOVIE, movie);
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
        favMovieHelper.close();
    }

}
