package mm.kso.movieapplication.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import mm.kso.movieapplication.db.FavoriteDao;
import mm.kso.movieapplication.db.FavoriteMovie;
import mm.kso.movieapplication.model.Actor;
import mm.kso.movieapplication.model.Movie;
import mm.kso.movieapplication.model.MovieResponse;
import mm.kso.movieapplication.network.MovieApiService;

public class Repository {
    public static final String TAG = "Repository";

    MovieApiService movieApiService;
    FavoriteDao favoriteDao;

    @Inject
    public Repository(MovieApiService movieApiService, FavoriteDao favoriteDao) {
        this.movieApiService = movieApiService;
        this.favoriteDao = favoriteDao;
    }


    public Observable<MovieResponse> getCurrentlyShowing(HashMap<String, String> map){
        return movieApiService.getCurrentlyShowing(map);
    }

    public Observable<MovieResponse>  getPopular(HashMap<String, String> map){
        return movieApiService.getPopular(map);
    }

    public Observable<MovieResponse>  getTopRated(HashMap<String, String> map){
        return movieApiService.getTopRated(map);
    }

    public Observable<MovieResponse>  getUpcoming(HashMap<String, String> map){
        return movieApiService.getUpcoming(map);
    }

    public Observable<Movie>  getMovieDetails(int movieId, HashMap<String, String> map){
        return movieApiService.getMovieDetails(movieId, map);
    }

    public Observable<JsonObject>  getCast(int movieId, HashMap<String, String> map){
        return movieApiService.getCast(movieId,map);
    }

    public Observable<Actor>  getActorDetails(int personId, HashMap<String,String> map){
        return movieApiService.getActorDetails(personId,map);
    }

    public Observable<JsonObject> getMoviesBySearch( HashMap<String, String> map){
        return movieApiService.getMoviesBySearch(map);

    }

    public void insertMovie(FavoriteMovie favoriteMovie){
        Log.e(TAG, "insertMovie: " );
        favoriteDao.insert(favoriteMovie);
    }

    public void deleteMovie(int movieId){
        favoriteDao.delete(movieId);
    }

    public void clearFavoriteList(){
        favoriteDao.clearFavoriteList();
    }

    public LiveData<List<FavoriteMovie>> getFavoriteList(){
        return  favoriteDao.getFavoriteList();
    }

    public FavoriteMovie getFavoriteListMovie(int movieId){
        return favoriteDao.getFavoriteListMovie(movieId);
    }


}
