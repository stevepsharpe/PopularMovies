package pe.shar.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by steve on 03/04/2017.
 */

public class FavoriteService {

    private static final String TAG = FavoriteService.class.getSimpleName();
    private Context mContext;

    public FavoriteService(Context mContext) {
        this.mContext = mContext;
    }

    public void addFavorite(long movieId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.Favorite.COLUMN_MOVIE_ID, movieId);
        mContext.getContentResolver().insert(MoviesContract.Favorite.CONTENT_URI, contentValues);
    }

    public void removeFavorite(long movieId) {
        mContext.getContentResolver().delete(
                MoviesContract.Favorite.CONTENT_URI,
                MoviesContract.Favorite.COLUMN_MOVIE_ID + " = ?",
                new String[]{ String.valueOf(movieId) }
        );
    }

    public boolean isFavorite(long movieId) {
        boolean isFavorite = false;

        Cursor cursor = mContext.getContentResolver().query(
                MoviesContract.Favorite.CONTENT_URI,
                null,
                MoviesContract.Favorite.COLUMN_MOVIE_ID + " = ?",
                new String[]{ String.valueOf(movieId) },
                null,
                null
        );

        if (cursor != null) {
            Log.d(TAG, "isFavorite: Cursor is not null " + cursor.getCount());
            isFavorite = cursor.getCount() != 0;
            cursor.close();
        }

        return isFavorite;
    }
}
