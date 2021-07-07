package mm.kso.movieapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import mm.kso.movieapplication.db.FavoriteDao
import mm.kso.movieapplication.db.FavoriteMovie
import mm.kso.movieapplication.model.Actor
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.model.MovieResponse
import mm.kso.movieapplication.network.MovieApiService
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    var movieApiService: MovieApiService,
    var favoriteDao: FavoriteDao
) {
    fun getCurrentlyShowing(map: HashMap<String, String>): Observable<MovieResponse?>? {
        return movieApiService.getCurrentlyShowing(map)
    }

    fun getPopular(map: HashMap<String, String>): Observable<MovieResponse?>? {
        return movieApiService.getPopular(map)
    }

    fun getTopRated(map: HashMap<String, String>): Observable<MovieResponse?>? {
        return movieApiService.getTopRated(map)
    }

    fun getUpcoming(map: HashMap<String, String>): Observable<MovieResponse?>? {
        return movieApiService.getUpcoming(map)
    }

    fun getMovieDetails(movieId: Int, map: HashMap<String?, String?>?): Observable<Movie?>? {
        return movieApiService.getMovieDetails(movieId, map)
    }

    fun getCast(movieId: Int, map: HashMap<String?, String?>?): Observable<JsonObject?>? {
        return movieApiService.getCast(movieId, map)
    }

    fun getActorDetails(personId: Int, map: HashMap<String?, String?>?): Observable<Actor?>? {
        return movieApiService.getActorDetails(personId, map)
    }

    fun getMoviesBySearch(map: HashMap<String?, String?>?): Observable<JsonObject?>? {
        return movieApiService.getMoviesBySearch(map)
    }

    fun insertMovie(favoriteMovie: FavoriteMovie) {
        Log.e(TAG, "insertMovie: ")
        favoriteDao.insert(favoriteMovie)
    }

    fun deleteMovie(movieId: Int) {
        favoriteDao.delete(movieId)
    }

    fun clearFavoriteList() {
        favoriteDao.clearFavoriteList()
    }

    val favoriteList: LiveData<List<FavoriteMovie>>
        get() = favoriteDao.favoriteList

    fun getFavoriteListMovie(movieId: Int): FavoriteMovie{
        return favoriteDao.getFavoriteListMovie(movieId)
    }

    companion object {
        const val TAG = "Repository"
    }
}