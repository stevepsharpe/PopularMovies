package pe.shar.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.shar.popularmovies.R;
import pe.shar.popularmovies.adapter.MoviesAdapter;
import pe.shar.popularmovies.data.Movie;

public class MovieListActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler {

    private static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            MovieListFragment movieListFragment = new MovieListFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.movies_container, movieListFragment, "movieListFragment");
            transaction.commit();
        }

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        startActivity(intent);
    }
}
