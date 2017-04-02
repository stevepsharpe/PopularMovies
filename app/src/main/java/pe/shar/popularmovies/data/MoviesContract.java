package pe.shar.popularmovies.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by steve on 01/04/2017.
 */

public class MoviesContract {
    public static final String AUTHORITY = "pe.shar.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_FAVORITES = "favorites";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

//        The MIME type of a directory of events
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.popularmovies.movie";

//        The MIME type of a single event
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.popularmovies.movie";

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_HOMEPAGE = "homepage";
        public static final String COLUMN_IMDB_ID = "imdb_id";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_REVENUE = "revenue";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_TAGLINE = "tagline";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
    }

    public static final class Favorite implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();


        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.popularmovies.favorite";

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movie_id";

    }
}
