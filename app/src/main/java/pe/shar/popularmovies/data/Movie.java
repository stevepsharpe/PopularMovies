package pe.shar.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by steve on 04/03/2017.
 */

public class Movie implements Parcelable {

    private static final String TAG = Movie.class.getSimpleName();

    @SerializedName("id")
    private long id;

    @SerializedName("tile")
    private String title;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("adult")
    private Boolean adult;

    @SerializedName("popularity")
    private double popularity;

    @Override
    public String toString() {
        return id + " " + title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public Movie(
            long id,
            String title,
            String originalTitle,
            String posterPath,
            String backdropPath,
            String overview,
            double voteAverage,
            int voteCount,
            String releaseDate,
            Boolean adult,
            double popularity
    ) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.releaseDate = releaseDate;
        this.adult = adult;
        this.popularity = popularity;
    }

    protected Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        voteCount = in.readInt();
        releaseDate = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeInt(voteCount);
        dest.writeString(releaseDate);
    }

    public static final class Response {
        public int page;
        public int total_results;
        public int total_pages;

        @SerializedName("results")
        public List<Movie> movies;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, getId());
        values.put(MoviesContract.MovieEntry.COLUMN_TITLE, getTitle());
        values.put(MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE, getOriginalTitle());
        values.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, getPosterPath());
        values.put(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH, getBackdropPath());
        values.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, getOverview());
        values.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, getVoteAverage());
        values.put(MoviesContract.MovieEntry.COLUMN_VOTE_COUNT, getVoteCount());
        values.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, getReleaseDate());
        values.put(MoviesContract.MovieEntry.COLUMN_ADULT, getAdult());
        values.put(MoviesContract.MovieEntry.COLUMN_POPULARITY, getPopularity());
        return values;
    }

    public static Movie fromCursor(Cursor cursor) {
        Log.d(TAG, "fromCursor: " + cursor);
        long movieId = cursor.getLong(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_MOVIE_ID));
        String title = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE));
        String originalTitle = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
        String posterPath = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH));
        String backdropPath = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH));
        String overview = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW));
        double voteAverage = cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE));
        int voteCount = cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_COUNT));
        String releaseDate = cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE));
        Boolean adult = cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ADULT)) > 0;
        double popularity = cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POPULARITY));

        return new Movie(
                movieId,
                title,
                originalTitle,
                posterPath,
                backdropPath,
                overview,
                voteAverage,
                voteCount,
                releaseDate,
                adult,
                popularity
        );
    }
}
