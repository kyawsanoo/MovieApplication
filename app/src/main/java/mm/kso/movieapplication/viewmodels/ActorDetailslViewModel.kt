package mm.kso.movieapplication.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import mm.kso.movieapplication.model.Actor
import mm.kso.movieapplication.repository.Repository
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ActorDetailslViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val disposable = CompositeDisposable()
    val actor = MutableLiveData<Actor>()
    fun getActorDetails(personId: Int, map: HashMap<String?, String?>?) {
        disposable.add(
            repository.getActorDetails(personId, map)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { result: Actor? -> actor.setValue(result) }
                ) { error: Throwable -> Log.e(TAG, "getActorDetails: " + error.message) }
        )
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}