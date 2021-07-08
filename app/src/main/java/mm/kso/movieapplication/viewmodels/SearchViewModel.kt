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
import mm.kso.movieapplication.model.Actor
import mm.kso.movieapplication.model.Cast
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.repository.Repository
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val disposable = CompositeDisposable()
    val queriesMovies = MutableLiveData<ArrayList<Movie>>()
    private val actorDetails = MutableLiveData<Actor>()

    fun getActorDetails(personId: Int, map: HashMap<String, String>) {
        val add = disposable.add(
            repository.getActorDetails(personId, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result: Actor -> actorDetails.setValue(result) }
                ) { error: Throwable -> Log.e(TAG, "getActorDetails: " + error.message) }
        )
    }

    fun getQueriedMovies(map: HashMap<String, String>) {
        disposable.add(
            repository.getMoviesBySearch(map)
                .subscribeOn(Schedulers.io())
                .map<ArrayList<Movie>> { jsonObject ->
                    val jsonArray = jsonObject?.getAsJsonArray("results")
                    Gson().fromJson(
                        jsonArray.toString(),
                        object : TypeToken<ArrayList<Movie?>?>() {}.type
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result: ArrayList<Movie> -> queriesMovies.setValue(result) }
                ) { error: Throwable -> Log.e(TAG, "getPopularMovies: " + error.message) }
        )
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}