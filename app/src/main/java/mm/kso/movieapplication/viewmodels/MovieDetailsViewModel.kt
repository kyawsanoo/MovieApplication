package mm.kso.movieapplication.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import mm.kso.movieapplication.db.FavoriteMovie
import mm.kso.movieapplication.model.Actor
import mm.kso.movieapplication.model.Cast
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.repository.Repository
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val disposable = CompositeDisposable()
    val movie = MutableLiveData<Movie>()
    val movieCastList = MutableLiveData<ArrayList<Cast>>()
    private val actorDetails = MutableLiveData<Actor>()

    fun getMovieDetails(movieId: Int, map: HashMap<String, String>) {
        disposable.add(
            repository.getMovieDetails(movieId, map)
                .subscribeOn(Schedulers.io())
                .map { movie ->
                    val genreNames = ArrayList<String>()
                    // MovieResponse gives list of genre(object) so we will map each id to it genre name here.a
                    for (genre in movie!!.genres) {
                        genreNames.add(genre.name)
                    }
                    movie.genre_names = genreNames
                    movie
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result: Movie? -> movie.setValue(result) }
                ) { error: Throwable -> Log.e(TAG, "getMovieDetails: " + error.message) }
        )
    }

    fun getCast(movieId: Int, map: HashMap<String, String>) {
        disposable.add(
            repository.getCast(movieId, map)
                .subscribeOn(Schedulers.io())
                .map { jsonObject ->
                    val jsonArray = jsonObject?.getAsJsonArray("cast")
                    Gson().fromJson<ArrayList<Cast>>(
                        jsonArray.toString(),
                        object : TypeToken<ArrayList<Cast?>?>() {}.type
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result: ArrayList<Cast> -> movieCastList.setValue(result) }
                ) { error: Throwable -> Log.e(TAG, "getCastList: " + error.message) }
        )
    }

    fun getActorDetails(personId: Int, map: HashMap<String, String>) {
        disposable.add(
            repository.getActorDetails(personId, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result: Actor? -> actorDetails.setValue(result) }
                ) { error: Throwable -> Log.e(TAG, "getActorDetails: " + error.message) }
        )
    }

    fun getFavoriteListMovie(movieId: Int): FavoriteMovie? {
        return repository.getFavoriteListMovie(movieId)
    }

    fun insertMovie(favoriteMovie: FavoriteMovie) {
        Log.e(TAG, "insertMovie: ")
        repository.insertMovie(favoriteMovie)
    }

    fun deleteMovie(movieId: Int) {
        repository.deleteMovie(movieId)
    }

    companion object {
        private const val TAG = "MovieDetailViewModel"
    }
}