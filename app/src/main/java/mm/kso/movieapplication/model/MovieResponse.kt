package mm.kso.movieapplication.model

import java.util.*

class MovieResponse(
    var page: Int,
    var total_pages: Int,
    var total_results: Int,
    var results: ArrayList<Movie>
)