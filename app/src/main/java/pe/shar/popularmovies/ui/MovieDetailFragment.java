package pe.shar.popularmovies.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.shar.popularmovies.R;
import pe.shar.popularmovies.adapter.ReviewsAdapter;
import pe.shar.popularmovies.adapter.VideosAdapter;
import pe.shar.popularmovies.api.ServiceGenerator;
import pe.shar.popularmovies.api.TMDBApiClient;
import pe.shar.popularmovies.data.Movie;
import pe.shar.popularmovies.data.Review;
import pe.shar.popularmovies.data.Video;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by steve on 08/03/2017.
 */

public class MovieDetailFragment extends Fragment {

    private static final String TAG = MovieDetailFragment.class.getSimpleName();

    public static final String IMG_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String POSTER_IMG_SIZE = "w500";
    public static final String BACKDROP_IMG_SIZE = "w780";
    private static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    private Movie mMovie;

    @BindView(R.id.movie_poster)
    ImageView mMoviePoster;

    @BindView(R.id.movie_title)
    TextView mMovieTitle;

    @BindView(R.id.movie_overview)
    TextView mMovieOverview;

    @BindView(R.id.movie_year)
    TextView mMovieYear;

    @BindView(R.id.vote_avg)
    TextView mVoteAvg;

    @BindView(R.id.videos_text_view)
    TextView mVideosTextView;

    @BindView(R.id.videos_recycler_view)
    RecyclerView mVideosRecyclerView;

    @BindView(R.id.reviews_text_view)
    TextView mReviewsTextView;

    @BindView(R.id.reviews_recycler_view)
    RecyclerView mReviewsRecyclerView;

    private LinearLayoutManager mVideosLinearLayoutManager;
    private LinearLayoutManager mReviewsLinearLayoutManager;
    private ReviewsAdapter mReviewsAdapter;
    private VideosAdapter mVideosAdapter;
    private TMDBApiClient mClient;

    private boolean mHideMenu = false;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMovie = bundle.getParcelable(EXTRA_MOVIE);
        }

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);

        mVideosLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mReviewsLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mReviewsAdapter = new ReviewsAdapter(getActivity(), null);
        mVideosAdapter = new VideosAdapter(getActivity(), null, (VideosAdapter.VideoAdapterOnClickHandler) getActivity());
        mClient = ServiceGenerator.createService(TMDBApiClient.class);

        mVideosRecyclerView.setLayoutManager(mVideosLinearLayoutManager);
        mVideosRecyclerView.setAdapter(mVideosAdapter);

        mReviewsRecyclerView.setLayoutManager(mReviewsLinearLayoutManager);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        if (mMovie != null) {
            mMovieTitle.setText(mMovie.getOriginalTitle());
            mMovieOverview.setText(mMovie.getOverview());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
            try {
                Date d = dateFormat.parse(mMovie.getReleaseDate());
                Log.d(TAG, "onCreateView: " + mMovie.getReleaseDate());
                mMovieYear.setText(yearFormat.format(d));
            } catch (ParseException e) {
                mMovieYear.setVisibility(View.GONE);
                e.printStackTrace();
            }

            mVoteAvg.setText(String.format(Locale.getDefault(), "%s/%s", getFormattedAverage(mMovie.getVoteAverage()), "10"));

            if (mMovie.getPosterPath() == null) {
                mMoviePoster.setVisibility(View.GONE);
            } else {
                Glide.with(this)
                        .load(IMG_BASE_URL + BACKDROP_IMG_SIZE + mMovie.getPosterPath())
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // caches all versions of the image
                        .crossFade()
                        .fitCenter()
                        .into(mMoviePoster);
            }

            loadVideos(mMovie.getId());
            loadReviews(mMovie.getId(), 1);
        }

        return view;
    }

    public String getFormattedAverage(Double average) {
        if (average % 1 == 0) {
            return String.format(Locale.getDefault(), "%.0f", average);
        } else {
            return String.format(Locale.getDefault(), "%.1f", average);
        }
    }

    public void loadVideos(long movieId) {
        Call<Video.Response> call = mClient.getMovieVideos(movieId);
        call.enqueue(new Callback<Video.Response>() {
            @Override
            public void onResponse(Call<Video.Response> call, Response<Video.Response> response) {
                Video.Response videosResponse = response.body();

                Log.d(TAG, "onResponse: " + videosResponse.videos);

                // Don't take up any space if there aren't any videos
                if (videosResponse.videos.size() == 0) {
                    mVideosTextView.setVisibility(View.GONE);
                    mVideosRecyclerView.setVisibility(View.GONE);
                    mHideMenu = true;
                    getActivity().invalidateOptionsMenu();
                }

                mVideosAdapter.setVideos(videosResponse.videos);
            }

            @Override
            public void onFailure(Call<Video.Response> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                Snackbar.make(getView(), "Error while fetching videos", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void loadReviews(long movieId, int page) {
        Call<Review.Response> call = mClient.getMovieReviews(movieId, page);
        call.enqueue(new Callback<Review.Response>() {
            @Override
            public void onResponse(Call<Review.Response> call, Response<Review.Response> response) {
                Review.Response reviewsResponse = response.body();
                Log.d(TAG, "onResponse: " + reviewsResponse.reviews);

                // Don't take up any space if there aren't any videos
                if (reviewsResponse.reviews.size() == 0) {
                    mReviewsTextView.setVisibility(View.GONE);
                    mReviewsRecyclerView.setVisibility(View.GONE);
                }

                mReviewsAdapter.setReviews(reviewsResponse.reviews);
            }

            @Override
            public void onFailure(Call<Review.Response> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
                Snackbar.make(getView(), "Error while fetching reviews", Snackbar.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_detail, menu);
        // Visible by default. Hidden if no videos exists
        menu.getItem(0).setVisible(!mHideMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_share) {
            showShareDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showShareDialog() {
        // Using Intent.createChooser because
        // ShareActionProvider didn't seem to play nice with material
        String shareSubject = "%s - %s";
        Video videoToShare = mVideosAdapter.getShareableVideo();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, String.format(shareSubject, mMovie.getOriginalTitle(), videoToShare.getName()));
        intent.putExtra(Intent.EXTRA_TEXT, videoToShare.getUrl());

        startActivity(Intent.createChooser(intent, getString(R.string.menu_share)));
    }
}
