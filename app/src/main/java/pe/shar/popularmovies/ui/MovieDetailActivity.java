package pe.shar.popularmovies.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pe.shar.popularmovies.R;
import pe.shar.popularmovies.adapter.VideosAdapter;
import pe.shar.popularmovies.data.FavoriteService;
import pe.shar.popularmovies.data.Movie;
import pe.shar.popularmovies.data.Video;

public class MovieDetailActivity extends AppCompatActivity implements VideosAdapter.VideoAdapterOnClickHandler {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    public static final String IMG_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String POSTER_IMG_SIZE = "w500";
    public static final String BACKDROP_IMG_SIZE = "w780";

    private Movie mMovie;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.backdrop_image)
    ImageView movieBackdrop;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    FavoriteService mFavoriteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        mFavoriteService = new FavoriteService(this);

        mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(mMovie.getOriginalTitle());
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
        setTitle("");

        setFabIcon();

        Glide.with(this)
                .load(IMG_BASE_URL + BACKDROP_IMG_SIZE + mMovie.getBackdropPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL) // caches all versions of the image
                .crossFade()
                .fitCenter()
                .into(movieBackdrop);


        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(bundle);

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            bundle.putParcelable(EXTRA_MOVIE, mMovie);
            transaction.replace(R.id.movie_container, movieDetailFragment, "movieDetailFragment");
            transaction.commit();
        }
    }

    @OnClick(R.id.fab)
    void handleFabClicked() {
        if (mFavoriteService.isFavorite(mMovie.getId())) {
            mFavoriteService.removeFavorite(mMovie.getId());
            showSnackbar("Favorite Removed");
        } else {
            mFavoriteService.addFavorite(mMovie.getId());
            showSnackbar("Favorite Added");
        }

        setFabIcon();
    }

    private void setFabIcon() {
        if (mFavoriteService.isFavorite(mMovie.getId())) {
            fab.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Video video) {
        Log.d(TAG, "onClick: ");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getUrl()));
        startActivity(intent);
    }
}
