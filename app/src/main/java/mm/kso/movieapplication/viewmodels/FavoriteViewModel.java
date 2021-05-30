package mm.kso.movieapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import mm.kso.movieapplication.db.FavoriteMovie;
import mm.kso.movieapplication.repository.Repository;

@HiltViewModel
public class FavoriteViewModel extends ViewModel {
    private Repository repository;
    private LiveData<List<FavoriteMovie>> favoriteMoviesList;

    @Inject
    public FavoriteViewModel(Repository repository) {
        this.repository = repository;
        favoriteMoviesList = repository.getFavoriteList();
    }

    public LiveData<List<FavoriteMovie>> getFavoriteMoviesList() {
        return favoriteMoviesList;
    }

    public void clearWishList(){
        repository.clearFavoriteList();
    }
}
