package pe.shar.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by steve on 01/04/2017.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + MoviesContract.MovieEntry.TABLE_NAME + " ("
                + MoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL,"
                + MoviesContract.MovieEntry.COLUMN_ADULT + " INTEGER NOT NULL,"
                + MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT,"
                + MoviesContract.MovieEntry.COLUMN_HOMEPAGE + " TEXT,"
                + MoviesContract.MovieEntry.COLUMN_IMDB_ID + " INTEGER,"
                + MoviesContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT,"
                + MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT,"
                + MoviesContract.MovieEntry.COLUMN_OVERVIEW + " TEXT,"
                + MoviesContract.MovieEntry.COLUMN_POPULARITY + " REAL,"
                + MoviesContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT,"
                + MoviesContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT,"
                + MoviesContract.MovieEntry.COLUMN_REVENUE + " INTEGER,"
                + MoviesContract.MovieEntry.COLUMN_RUNTIME + " INTEGER,"
                + MoviesContract.MovieEntry.COLUMN_STATUS + " TEXT,"
                + MoviesContract.MovieEntry.COLUMN_TAGLINE + " TEXT,"
                + MoviesContract.MovieEntry.COLUMN_TITLE + " TEXT,"
                + MoviesContract.MovieEntry.COLUMN_VIDEO + " INTEGER,"
                + MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL,"
                + MoviesContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER,"
                + "UNIQUE (" + MoviesContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE)");
    }

    /**
     *
     * @param db Database that is being upgraded.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do this just whilst in development
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
