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
import mm.kso.movieapplication.model.Actor;
import mm.kso.movieapplication.model.Cast;
import mm.kso.movieapplication.model.Movie;
import mm.kso.movieapplication.repository.Repository;

@HiltViewModel
public class SearchViewModel extends ViewModel {

    private static final String TAG = "SearchViewModel";
    private Repository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<ArrayList<Movie>> queriesMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Cast>> movieCastList = new MutableLiveData<>();
    private MutableLiveData<Actor> actorDetails = new MutableLiveData<>();

    @Inject
    public SearchViewModel(Repository repository) {
        this.repository = repository;
    }


    public MutableLiveData<ArrayList<Movie>> getQueriesMovies() {
        return queriesMovies;
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


    public void getQueriedMovies(HashMap<String, String> map){
        disposable.add(repository.getMoviesBySearch(map)
                .subscribeOn(Schedulers.io())
                .map(new Function<JsonObject, ArrayList<Movie>>() {
                         @Override
                         public ArrayList<Movie> apply(JsonObject jsonObject) throws Throwable {
                             JsonArray jsonArray = jsonObject.getAsJsonArray("results");
                             ArrayList<Movie> movieList = new Gson().fromJson(jsonArray.toString(),
                                     new TypeToken<ArrayList<Movie>>(){}.getType());
                             return movieList;
                         }
                     }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result->queriesMovies.setValue(result),
                        error-> Log.e(TAG, "getPopularMovies: " + error.getMessage() ))
        );
    }

}