package mm.kso.movieapplication.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import mm.kso.movieapplication.network.AuthInterceptor;
import mm.kso.movieapplication.utils.Constants;
import mm.kso.movieapplication.network.MovieApiService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    @Provides
    @Singleton
    public static MovieApiService provideMovieApiService(Retrofit retrofit){
        return  retrofit.create(MovieApiService.class);
    }

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

    }

    @Provides
    @Singleton
    public static OkHttpClient provideOkHttpClient(AuthInterceptor authInterceptor){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(authInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public static AuthInterceptor provideAuthInterceptor(){
        return new AuthInterceptor();
    }
}
