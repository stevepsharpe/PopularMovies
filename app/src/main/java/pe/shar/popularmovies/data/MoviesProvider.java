package pe.shar.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import pe.shar.popularmovies.data.MoviesContract.MovieEntry;

/**
 * Created by steve on 01/04/2017.
 */

public class MoviesProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    MoviesDbHelper mMoviesDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.AUTHORITY;

        /* This URI is pe.shar.popularmovies.data/movies/ */
        matcher.addURI(authority, MoviesContract.PATH_MOVIES, MOVIES);

        /* This URI is pe.shar.popularmovies.data/movies/123456 */
        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMoviesDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case MOVIES: {
                retCursor = mMoviesDbHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case MOVIE_WITH_ID: {
                retCursor = mMoviesDbHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_NAME,
                        projection,
                        MovieEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }



            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                return MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                long _id = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    return MoviesContract.MovieEntry.buildMovieUri(_id);
                }  else {
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        int rowsUpdated;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                rowsUpdated = db.update(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();

        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                numRowsDeleted = db.delete(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case MOVIE_WITH_ID:
                long id = MoviesContract.MovieEntry.getIdFromUri(uri);
                numRowsDeleted = db.delete(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        MovieEntry._ID  + " = ? ",
                        new String[]{Long.toString(id)}
                );

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = mMoviesDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(
                                MoviesContract.MovieEntry.TABLE_NAME,
                                null,
                                value,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}