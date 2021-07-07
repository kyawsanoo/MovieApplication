package mm.kso.movieapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;

public class Constants {

    public static final String BaseURL = "https://api.themoviedb.org/3/";
    public static final String ImageBaseURL = "https://image.tmdb.org/t/p/original";
    public static final String ImageBaseURLw500 = "https://image.tmdb.org/t/p/w500";
    public static final String DataBaseName = "FavoriteDB";

    public static final String Popular = "Popular";
    public static final String Upcoming = "Upcoming";
    public static final String Current = "Current";
    public static final String TopRated = "TopRated";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
        return isMobileConn || isWifiConn;
    }


    public static HashMap<Integer,String> getGenreMap(){
        HashMap<Integer,String> genreMap = new HashMap<>();
        genreMap.put(28,"Action");
        genreMap.put(12,"Adventure");
        genreMap.put(16,"Animation");
        genreMap.put(35,"Comedy");
        genreMap.put(80,"Crime");
        genreMap.put(99,"Documentary");
        genreMap.put(18,"Drama");
        genreMap.put(10751,"Family");
        genreMap.put(14,"Fantasy");
        genreMap.put(36,"History");
        genreMap.put(27,"Horror");
        genreMap.put(10402,"Music");
        genreMap.put(9648,"Mystery");
        genreMap.put(10749,"Romance");
        genreMap.put(878,"Science Fiction");
        genreMap.put(53,"Thriller");
        genreMap.put(10752,"War");
        genreMap.put(37,"Western");
        genreMap.put(10770,"TV Movie");

        return genreMap;
    }
}
