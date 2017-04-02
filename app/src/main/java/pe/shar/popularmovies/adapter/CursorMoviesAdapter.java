package pe.shar.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.shar.popularmovies.R;
import pe.shar.popularmovies.data.Movie;
import pe.shar.popularmovies.data.MoviesContract;

/**
 * Created by steve on 02/04/2017.
 */

public class CursorMoviesAdapter extends RecyclerView.Adapter<CursorMoviesAdapter.MovieViewHolder> {

    private static final String TAG = CursorMoviesAdapter.class.getSimpleName();
    private Cursor mCursor;
    private Context mContext;

    public static final String POSTER_IMG_BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String POSTER_IMG_SIZE = "w500";

    private final MoviesAdapter.MovieAdapterOnClickHandler mClickHandler;

    public CursorMoviesAdapter(Context context, Cursor cursor, MoviesAdapter.MovieAdapterOnClickHandler clickHandler) {
        Log.d(TAG, "CursorMoviesAdapter: Constructor called");
        this.mContext = context;
        this.mCursor = cursor;
        this.mClickHandler = clickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: New view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: starts");

        if (mCursor == null || mCursor.getCount() == 0) {
            Log.d(TAG, "onBindViewHolder: no items ");
        }

        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Couldn't move cursor to position " + position);
        }


//        holder.cursor = mCursor;

        String posterPath = mCursor.getString(mCursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH));

        Glide.with(mContext)
                .load(POSTER_IMG_BASE_URL + POSTER_IMG_SIZE + posterPath)
                .placeholder(new ColorDrawable(mContext.getResources().getColor(R.color.colorPrimary)))
                .diskCacheStrategy(DiskCacheStrategy.ALL) // caches all versions of the image
                .crossFade()
                .fitCenter()
                .into(holder.moviePoster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");

                if (mCursor.moveToPosition(holder.getAdapterPosition())) {
                    Movie movie = Movie.fromCursor(mCursor);
                    Log.d(TAG, "moveToPosition: " + movie);
                    mClickHandler.onClick(movie);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    /**
     * Swap in a new Cursor, returning the old Cursor
     * The returned old Cursor if <em>not</em> closed.
     * @param newCursor The new Cursor to be used
     * @return Returns the previously set Cursor, or null if there isn't one
     * If the given new Cursor is the same as the previously set
     * Cursor, null is also returned.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }

        final Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            // notify the observers about the lack of a data set
            notifyItemRangeRemoved(0, getItemCount());
        }

        return oldCursor;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = MovieViewHolder.class.getSimpleName();

        @BindView(R.id.img_movie)
        ImageView moviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "MovieViewHolder: ");
            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            Log.d(TAG, "onClick: ");
//
//            int position = this.getAdapterPosition();
//
//            mCursorAdapter.getCursor().moveToPosition(pos)
//
//            mCursor.moveToPosition(position);
////            if () {
////
////            }
//            Movie movie = Movie.fromCursor(mCursor);
//            mClickHandler.onClick(movie);
//        }
    }
}
