package mm.kso.movieapplication.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import mm.kso.movieapplication.db.FavoriteMovie;
import mm.kso.movieapplication.model.Actor;
import mm.kso.movieapplication.model.Cast;
import mm.kso.movieapplication.model.Genre;
import mm.kso.movieapplication.model.Movie;
import mm.kso.movieapplication.repository.Repository;

@HiltViewModel
public class MovieDetailsViewModel extends ViewModel {

    private static final String TAG = "MovieDetailViewModel";
    private Repository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Movie> movieDetails = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Cast>> movieCastList = new MutableLiveData<>();
    private MutableLiveData<Actor> actorDetails = new MutableLiveData<>();

    @Inject
    public MovieDetailsViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<Movie> getMovie() {
        return movieDetails;
    }

    public MutableLiveData<ArrayList<Cast>> getMovieCastList() {
        return movieCastList;
    }


    public void getMovieDetails(int movieId, HashMap<String, String> map) {
        disposable.add(repository.getMovieDetails(movieId, map)
                .subscribeOn(Schedulers.io())
                .map(new Function<Movie, Movie>() {
                    @Override
                    public Movie apply(Movie movie) throws Throwable {
                        ArrayList<String> genreNames = new ArrayList<>();
                        // MovieResponse gives list of genre(object) so we will map each id to it genre name here.a

                        for(Genre genre : movie.getGenres()){
                            genreNames.add(genre.getName());
                        }
                        movie.setGenre_names(genreNames);
                        return movie;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> movieDetails.setValue(result),
                        error -> Log.e(TAG, "getMovieDetails: " + error.getMessage()))
        );
    }


    public void getCast(int movieId, HashMap<String, String> map) {
        disposable.add(repository.getCast(movieId, map)
                .subscribeOn(Schedulers.io())
                .map(new Function<JsonObject, ArrayList<Cast>>() {
                    @Override
                    public ArrayList<Cast> apply(JsonObject jsonObject) throws Throwable {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("cast");
                        return  new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<Cast>>(){}.getType());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> movieCastList.setValue(result),
                        error -> Log.e(TAG, "getCastList: " + error.getMessage()))
        );
    }

    public void getActorDetails(int personId, HashMap<String,String> map) {
        disposable.add(repository.getActorDetails(personId, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> actorDetails.setValue(result),
                        error -> Log.e(TAG, "getActorDetails: " + error.getMessage()))
        );
    }

    public FavoriteMovie getFavoriteListMovie(int movieId){
        return  repository.getFavoriteListMovie(movieId);
    }

    public void insertMovie(FavoriteMovie favoriteMovie){
        Log.e(TAG, "insertMovie: " );
        repository.insertMovie(favoriteMovie);
    }

    public void deleteMovie(int movieId){
        repository.deleteMovie(movieId);
    }



}