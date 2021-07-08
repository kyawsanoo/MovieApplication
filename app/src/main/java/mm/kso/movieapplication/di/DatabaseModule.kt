package mm.kso.movieapplication.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mm.kso.movieapplication.db.FavoriteDao
import mm.kso.movieapplication.db.MovieDatabase
import mm.kso.movieapplication.utils.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(application: Application): MovieDatabase {
        return Room.databaseBuilder(
            application,
            MovieDatabase::class.java,
            Constants.DataBaseName
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(movieDatabase: MovieDatabase): FavoriteDao {
        return movieDatabase.favoriteDao()
    }
}