package mm.kso.movieapplication.model

import com.google.gson.JsonObject
import java.util.*

class Movie(
    var poster_path: String, var overview: String, var release_date: String, var title: String,
    var id: Int, var vote_count: Int, var popularity: Number, var vote_average: Number,
    var genre_ids: ArrayList<Int>, var runtime: Int, var genres: ArrayList<Genre>,
    var backdrop_path: String, var videos: JsonObject
) {
    var genre_names: ArrayList<String>? = null

}