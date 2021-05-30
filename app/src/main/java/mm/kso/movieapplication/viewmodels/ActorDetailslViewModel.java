package mm.kso.movieapplication.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import mm.kso.movieapplication.model.Actor;
import mm.kso.movieapplication.repository.Repository;

@HiltViewModel
public class ActorDetailslViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";
    private Repository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Actor> actorDetails = new MutableLiveData<>();

    @Inject
    public ActorDetailslViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<Actor> getActor() {
        return actorDetails;
    }

    public void getActorDetails(int personId, HashMap<String,String> map) {
        disposable.add(repository.getActorDetails(personId, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> actorDetails.setValue(result),
                        error -> Log.e(TAG, "getActorDetails: " + error.getMessage()))
        );
    }

}