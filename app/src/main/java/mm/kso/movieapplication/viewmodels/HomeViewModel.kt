package mm.kso.movieapplication.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.model.MovieResponse
import mm.kso.movieapplication.repository.Repository
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val currentlyShowingList = MutableLiveData<ArrayList<Movie>?>()
    private val disposable = CompositeDisposable()
    val movie = MutableLiveData<Movie>()
    val popularMoviesList = MutableLiveData<ArrayList<Movie>>()
    val topRatedMoviesList = MutableLiveData<ArrayList<Movie>>()
    val upcomingMoviesList = MutableLiveData<ArrayList<Movie>>()
    fun getCurrentlyShowingMovies(map: HashMap<String, String>) {
        disposable.add(
            repository.getCurrentlyShowing(map)
                ?.subscribeOn(Schedulers.io())
                ?.map { movieResponse -> movieResponse?.results }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<ArrayList<Movie>>() {
                    override fun onNext(movies: @NonNull ArrayList<Movie>?) {
                        currentlyShowingList.setValue(movies)
                    }

                    override fun onError(e: @NonNull Throwable?) {}
                    override fun onComplete() {}
                })
        )
    }

    fun getPopularMovies(map: HashMap<String, String>) {
        disposable.add(
            repository.getPopular(map)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { result: MovieResponse? -> popularMoviesList.setValue(result?.results) }
                ) { error: Throwable -> Log.e(TAG, "getPopularMovies: " + error.message) }
        )
    }

    fun getTopRatedMovies(map: HashMap<String, String>) {
        disposable.add(
            repository.getTopRated(map)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { result: MovieResponse? -> topRatedMoviesList.setValue(result?.results) }
                ) { error: Throwable -> Log.e(TAG, "getTopRated: " + error.message) }
        )
    }

    fun getUpcomingMovies(map: HashMap<String, String>) {
        disposable.add(
            repository.getUpcoming(map)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { result: MovieResponse? -> upcomingMoviesList.setValue(result?.results) }
                ) { error: Throwable -> Log.e(TAG, "getUpcoming: " + error.message) }
        )
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}