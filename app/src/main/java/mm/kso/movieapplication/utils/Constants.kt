package mm.kso.movieapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

object Constants {
    const val BaseURL = "https://api.themoviedb.org/3/"
    const val ImageBaseURL = "https://image.tmdb.org/t/p/original"
    const val ImageBaseURLw500 = "https://image.tmdb.org/t/p/w500"
    const val DataBaseName = "FavoriteDB"
    const val Popular = "Popular"
    const val Upcoming = "Upcoming"
    const val Current = "Current"
    const val TopRated = "TopRated"

    @JvmStatic
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun isNetworkAvailable(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isWifiConn = false
        var isMobileConn = false
        for (network in connMgr.allNetworks) {
            val networkInfo = connMgr.getNetworkInfo(network)
            if (networkInfo?.type == ConnectivityManager.TYPE_WIFI) {
                isWifiConn = isWifiConn or networkInfo.isConnected
            }
            if (networkInfo?.type == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn = isMobileConn or networkInfo.isConnected
            }
        }
        return isMobileConn || isWifiConn
    }

    @JvmStatic
    val genreMap: HashMap<Int, String>
        get() {
            val genreMap = HashMap<Int, String>()
            genreMap[28] = "Action"
            genreMap[12] = "Adventure"
            genreMap[16] = "Animation"
            genreMap[35] = "Comedy"
            genreMap[80] = "Crime"
            genreMap[99] = "Documentary"
            genreMap[18] = "Drama"
            genreMap[10751] = "Family"
            genreMap[14] = "Fantasy"
            genreMap[36] = "History"
            genreMap[27] = "Horror"
            genreMap[10402] = "Music"
            genreMap[9648] = "Mystery"
            genreMap[10749] = "Romance"
            genreMap[878] = "Science Fiction"
            genreMap[53] = "Thriller"
            genreMap[10752] = "War"
            genreMap[37] = "Western"
            genreMap[10770] = "TV Movie"
            return genreMap
        }
}