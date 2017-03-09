package pe.shar.popularmovies.api;

import pe.shar.popularmovies.data.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by steve on 04/03/2017.
 */

public interface TMDBApiClient {

    @GET("/3/discover/movie")
    Call<Movie.Response> getMovies(
            @Query("sort_by") String sort_by,
            @Query("page") int page
    );

    @GET("/3/movie/{id}")
    Call<Movie> getMovie(
            @Path("id") String id
    );
}
