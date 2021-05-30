package mm.kso.movieapplication.di;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import mm.kso.movieapplication.utils.Constants;
import mm.kso.movieapplication.db.FavoriteDao;
import mm.kso.movieapplication.db.MovieDatabase;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {


    @Provides
    @Singleton
    MovieDatabase provideMovieDatabase(Application application){
        return Room.databaseBuilder(application,MovieDatabase.class, Constants.DataBaseName)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    FavoriteDao provideFavoriteDao(MovieDatabase movieDatabase){
        return movieDatabase.favoriteDao();
    }
}
