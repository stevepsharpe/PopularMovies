package pe.shar.popularmovies.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.shar.popularmovies.R;
import pe.shar.popularmovies.data.Movie;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMovie = bundle.getParcelable(EXTRA_MOVIE);
        }

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, view);

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
}
