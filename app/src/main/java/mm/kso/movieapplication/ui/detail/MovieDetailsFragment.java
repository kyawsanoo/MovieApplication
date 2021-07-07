package mm.kso.movieapplication.ui.detail;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import mm.kso.movieapplication.BuildConfig;
import mm.kso.movieapplication.R;
import mm.kso.movieapplication.utils.Constants;
import mm.kso.movieapplication.adapters.CastAdapter;
import mm.kso.movieapplication.databinding.FragmentMovieDetailsBinding;
import mm.kso.movieapplication.db.FavoriteMovie;
import mm.kso.movieapplication.model.Cast;
import mm.kso.movieapplication.model.Movie;
import mm.kso.movieapplication.viewmodels.MovieDetailsViewModel;

@AndroidEntryPoint
public class MovieDetailsFragment extends Fragment{

    private static final String TAG = "MovieDetails";
    private FragmentMovieDetailsBinding binding;
    private MovieDetailsViewModel viewModel;
    private Integer movieId;
    private HashMap<String, String> queryMap;
    private String temp,videoId;
    private CastAdapter adapter;
    private ArrayList<Cast> castList;
    private int hour =0,min = 0;
    private Movie mMovie;

    private Boolean inFavList = false;
    private ArrayList<MediaStore.Video> videos;

    public MovieDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(MovieDetailsFragment.this).get(MovieDetailsViewModel.class);
        return view;
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        castList = new ArrayList<>();
        queryMap = new HashMap<>();

        MovieDetailsFragmentArgs args = MovieDetailsFragmentArgs.fromBundle(getArguments());
        movieId = args.getMovieId();

        observeData();
        //queryMap.put("api_key", BuildConfig.MOVIE_API_KEY);
        queryMap.put("page","1");
        queryMap.put("append_to_response","videos");

        viewModel.getMovieDetails(movieId,queryMap);
        viewModel.getCast(movieId,queryMap);

        binding.castRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        adapter = new CastAdapter(getContext(),castList);
        binding.castRecyclerView.setAdapter(adapter);
        binding.moviePoster.setClipToOutline(true);

        binding.addToFavoriteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inFavList){
                    viewModel.deleteMovie(movieId);
                    binding.addToFavoriteList.setImageResource(R.drawable.ic_playlist);
                    Toast.makeText(getContext(),"Removed from Favorite List.",Toast.LENGTH_SHORT).show();
                }
                else {
                    FavoriteMovie movie = new FavoriteMovie(mMovie.getId(),mMovie.getPoster_path(),mMovie.getOverview(),
                            mMovie.getRelease_date(),mMovie.getTitle(),mMovie.getBackdrop_path(),mMovie.getVote_count(),
                            mMovie.getRuntime());
                    viewModel.insertMovie(movie);
                    binding.addToFavoriteList.setImageResource(R.drawable.ic_playlist_add);
                    Toast.makeText(getContext(),"Added to Favorite List.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void isMovieInFavList(int movieId) {
        if(viewModel.getFavoriteListMovie(movieId) != null) {
            binding.addToFavoriteList.setImageResource(R.drawable.ic_playlist_add);
            inFavList = true;
        }
        else {
            binding.addToFavoriteList.setImageResource(R.drawable.ic_playlist);
            inFavList = false;
        }
        binding.addToFavoriteList.setVisibility(View.VISIBLE);
    }

    private void observeData() {
        viewModel.getMovie().observe(getViewLifecycleOwner(), new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                mMovie = movie;
                Glide.with(getContext()).load(Constants.ImageBaseURL + movie.getPoster_path())
                        .centerCrop()
                        .into(binding.moviePoster);

                binding.movieName.setText(movie.getTitle());

                hour = movie.getRuntime()/60;
                min = movie.getRuntime()%60;
                binding.movieRuntime.setText(hour+"h "+min+"m");
                binding.moviePlot.setText(movie.getOverview());
                temp = "";
                for (int i = 0; i < movie.getGenres().size(); i++){
                    if(i ==  movie.getGenres().size() -1)
                        temp+= movie.getGenres().get(i).getName();
                    else
                        temp+= movie.getGenres().get(i).getName() + " • ";
                }

                binding.movieGenre.setText(temp);
                binding.movieCastText.setVisibility(View.VISIBLE);
                binding.moviePlotText.setVisibility(View.VISIBLE);
                isMovieInFavList(movieId);

                JsonArray array = movie.getVideos().getAsJsonArray("results");
                videoId = array.get(0).getAsJsonObject().get("key").getAsString();
            }
        });

        viewModel.getMovieCastList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Cast>>() {
            @Override
            public void onChanged(ArrayList<Cast> actors) {
                Log.e(TAG, "onChanged: " + actors.size() );
                adapter.setCastList(actors);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                requireActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
 }


