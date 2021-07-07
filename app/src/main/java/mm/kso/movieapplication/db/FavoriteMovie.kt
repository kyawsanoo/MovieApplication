package mm.kso.movieapplication.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
class FavoriteMovie(
    @field:PrimaryKey(autoGenerate = true) var id: Int,
    var poster_path: String,
    var overview: String,
    var release_date: String,
    var title: String,
    var backdrop_path: String,
    var vote_count: Int,
    var runtime: Int
)