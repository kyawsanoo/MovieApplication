package mm.kso.movieapplication.network

import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import mm.kso.movieapplication.model.Actor
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import java.util.*

interface MovieApiService {
    @GET("movie/now_playing")
    fun getCurrentlyShowing(@QueryMap queries: HashMap<String, String>): Observable<MovieResponse?>?

    @GET("movie/popular")
    fun getPopular(@QueryMap queries: HashMap<String, String>): Observable<MovieResponse?>?

    @GET("movie/upcoming")
    fun getUpcoming(@QueryMap queries: HashMap<String, String>): Observable<MovieResponse?>?

    @GET("movie/top_rated")
    fun getTopRated(@QueryMap queries: HashMap<String, String>): Observable<MovieResponse?>?

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") id: Int,
        @QueryMap queries: HashMap<String?, String?>?
    ): Observable<Movie?>?

    @GET("movie/{movie_id}/credits")
    fun getCast(
        @Path("movie_id") id: Int,
        @QueryMap queries: HashMap<String?, String?>?
    ): Observable<JsonObject?>?

    @GET("person/{person_id}")
    fun getActorDetails(
        @Path("person_id") id: Int,
        @QueryMap queries: HashMap<String?, String?>?
    ): Observable<Actor?>?

    @GET("search/movie")
    fun getMoviesBySearch(@QueryMap queries: HashMap<String?, String?>?): Observable<JsonObject?>?
}