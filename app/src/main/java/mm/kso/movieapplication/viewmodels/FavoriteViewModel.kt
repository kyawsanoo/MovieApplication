package mm.kso.movieapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mm.kso.movieapplication.db.FavoriteMovie
import mm.kso.movieapplication.repository.Repository
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val favoriteMoviesList: LiveData<List<FavoriteMovie>> = repository.favoriteList
    fun clearWishList() {
        repository.clearFavoriteList()
    }

}