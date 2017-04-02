package pe.shar.popularmovies.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.shar.popularmovies.R;
import pe.shar.popularmovies.adapter.CursorMoviesAdapter;
import pe.shar.popularmovies.adapter.EndlessRecyclerViewScrollListener;
import pe.shar.popularmovies.adapter.MoviesAdapter;
import pe.shar.popularmovies.api.ServiceGenerator;
import pe.shar.popularmovies.api.TMDBApiClient;
import pe.shar.popularmovies.data.Movie;
import pe.shar.popularmovies.data.MoviesContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by steve on 07/03/2017.
 */

public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MovieListFragment.class.getSimpleName();
    private static final String STATE_MOVIES = "state_movies";
    private static final String STATE_SORT = "state_sort";
    private static final String SORT_POPULAR_MOVIES = "popularity.desc";
    private static final String SORT_TOP_MOVIES = "vote_average.desc";

    private static final int MOVIES_LOADER_ID = 88;
    private CursorMoviesAdapter mMoviesAdapter;


    private int mPosition = RecyclerView.NO_POSITION;

    private GridLayoutManager mGridLayoutManager;
//    private MoviesAdapter mMoviesAdapter;
    private TMDBApiClient mClient;

    private ArrayList<Movie> mMovies = new ArrayList<>();
    private String mSort = SORT_POPULAR_MOVIES;

    @BindView(R.id.movies_swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Log.d(TAG, "onCreate: savedInstanceState " + savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_MOVIES)) {
            mMovies = savedInstanceState.getParcelableArrayList(STATE_MOVIES);
            Log.d(TAG, "onCreate: mMovies " + mMovies);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_SORT)) {
            mSort = savedInstanceState.getString(STATE_SORT);
            Log.d(TAG, "onCreate: mSort " + mSort);
        }

        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mMoviesAdapter = new CursorMoviesAdapter(getActivity(), null, (MoviesAdapter.MovieAdapterOnClickHandler) getActivity());
        mClient = ServiceGenerator.createService(TMDBApiClient.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setAdapter(mMoviesAdapter);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    loadMovies(1);
                } else {
                    Snackbar.make(getView(), "Offline", Snackbar.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG, "onLoadMore: ");
                if (isNetworkAvailable()) {
                    loadMovies(page + 1);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        Log.d(TAG, "onCreateView: isConnected " + isConnected);

        if (isNetworkAvailable()) {
            loadMovies(1);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popular_movies:
                mSort = SORT_POPULAR_MOVIES;
                Log.d(TAG, "onOptionsItemSelected: mSort " + mSort);
                loadMovies(1);
                getActivity().setTitle(getResources().getString(R.string.action_popular));
                return true;
            case R.id.action_sort_top_movies:
                mSort = SORT_TOP_MOVIES;
                Log.d(TAG, "onOptionsItemSelected: mSort " + mSort);
                loadMovies(1);
                getActivity().setTitle(getResources().getString(R.string.action_top_rated));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState: mMovies " + mMovies);
        Log.d(TAG, "onSaveInstanceState: mSort " + mSort);

        outState.putParcelableArrayList(STATE_MOVIES, mMovies);
        outState.putString(STATE_SORT, mSort);
    }

    public void loadMovies(int page) {
        if (page == 1) {
            // TODO: Remove this - Ideally keep movies around and have another collection for popular / top / favs
            // and remove them rather than movies
            getContext().getContentResolver().delete(
                        MoviesContract.MovieEntry.CONTENT_URI, null, null);
            mMoviesAdapter.swapCursor(null);
        }

        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }

        Call<Movie.Response> call = mSort.equals(SORT_TOP_MOVIES) ? mClient.getTopMovies(page) : mClient.getPopularMovies(page);
        call.enqueue(new Callback<Movie.Response>() {
            @Override
            public void onResponse(Call<Movie.Response> call, Response<Movie.Response> response) {
                Movie.Response moviesResponse = response.body();

                Log.d(TAG, "onResponse: " + moviesResponse.movies);

                ContentValues[] contentValues = new ContentValues[moviesResponse.movies.size()];

                long mResults = moviesResponse.movies.size();

                for (int i = 0; i < mResults; i++) {
                    Movie movie = moviesResponse.movies.get(i);
                    contentValues[i] = movie.getContentValues();
                }

                getContext().getContentResolver().bulkInsert(
                        MoviesContract.MovieEntry.CONTENT_URI,
                        contentValues);

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Movie.Response> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                Snackbar.make(getView(), "Error while fetching data", Snackbar.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: starts with id " + id);

        String[] projection = {
                MoviesContract.MovieEntry._ID,
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
                MoviesContract.MovieEntry.COLUMN_TITLE,
                MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
                MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
                MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH,
                MoviesContract.MovieEntry.COLUMN_OVERVIEW,
                MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                MoviesContract.MovieEntry.COLUMN_VOTE_COUNT,
                MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
                MoviesContract.MovieEntry.COLUMN_ADULT,
                MoviesContract.MovieEntry.COLUMN_POPULARITY
        };

        String sortOrder = MoviesContract.MovieEntry.COLUMN_POPULARITY + " DESC";

        switch(id) {
            case MOVIES_LOADER_ID:
                return new CursorLoader(getActivity(),
                        MoviesContract.MovieEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: ");
        mMoviesAdapter.swapCursor(data);
        int count = mMoviesAdapter.getItemCount();

        Log.d(TAG, "onLoadFinished: count is " + count);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
        mMoviesAdapter.swapCursor(null);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
