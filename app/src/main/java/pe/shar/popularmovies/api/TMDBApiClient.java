package pe.shar.popularmovies.api;

import pe.shar.popularmovies.data.Movie;
import pe.shar.popularmovies.data.Review;
import pe.shar.popularmovies.data.Video;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by steve on 04/03/2017.
 */

public interface TMDBApiClient {

    @GET("movie/popular")
    Call<Movie.Response> getPopularMovies(
            @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<Movie.Response> getTopMovies(
            @Query("page") int page
    );

    @GET("movie/{id}")
    Call<Movie> getMovie(
            @Path("id") String id
    );

    @GET("movie/{id}/videos")
    Call<Video.Response> getMovieVideos(
            @Path("id") long id
    );

    @GET("movie/{id}/reviews")
    Call<Review.Response> getMovieReviews(
            @Path("id") long id,
            @Query("page") int page
    );
}
