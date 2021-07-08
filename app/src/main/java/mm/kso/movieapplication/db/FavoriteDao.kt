package mm.kso.movieapplication.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert
    fun insert(favoriteMovie: FavoriteMovie)

    @Query("DELETE From favorite_table WHERE id = :movieId")
    fun delete(movieId: Int)

    @Query("DELETE FROM favorite_table")
    fun clearFavoriteList()

    @get:Query("SELECT * FROM favorite_table")
    val favoriteList: LiveData<List<FavoriteMovie>>

    @Query("SELECT * FROM favorite_table WHERE id = :movieId ")
    fun getFavoriteListMovie(movieId: Int): FavoriteMovie?
}