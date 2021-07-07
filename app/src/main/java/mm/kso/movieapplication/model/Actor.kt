package mm.kso.movieapplication.model

import com.google.gson.JsonObject

class Actor(
    var birthday: String, var name: String, var biography: String, var place_of_birth: String,
    var profile_path: String, var known_for_department: String, var id: Int,
    var popularity: Number, var movie_credits: JsonObject
)