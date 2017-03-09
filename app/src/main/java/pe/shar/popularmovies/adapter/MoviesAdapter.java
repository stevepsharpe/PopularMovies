package pe.shar.popularmovies.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.shar.popularmovies.R;
import pe.shar.popularmovies.data.Movie;

/**
 * Created by steve on 04/03/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private List<Movie> mMovies;
    private Context context;

    public static final String POSTER_IMG_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String POSTER_IMG_SIZE = "w500";

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MoviesAdapter(Context context, List<Movie> movies, MovieAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.mMovies = movies;
        this.mClickHandler = clickHandler;
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "MoviesAdapterViewHolder";

        @BindView(R.id.img_movie)
        ImageView moviePoster;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            Movie movie = mMovies.get(getAdapterPosition());
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public MoviesAdapter.MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        Glide.with(context)
                .load(POSTER_IMG_BASE_URL + POSTER_IMG_SIZE + movie.getPosterPath())
                .placeholder(new ColorDrawable(context.getResources().getColor(R.color.colorPrimary)))
                .diskCacheStrategy(DiskCacheStrategy.ALL) // caches all versions of the image
                .crossFade()
                .fitCenter()
                .into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
        return mMovies.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies) {
        if (null == mMovies) {
            mMovies = movies;
        } else {
            mMovies.addAll(movies);
        }

        int itemCount = getItemCount();
        notifyItemRangeInserted(itemCount, movies.size() - 1);
    }
}
