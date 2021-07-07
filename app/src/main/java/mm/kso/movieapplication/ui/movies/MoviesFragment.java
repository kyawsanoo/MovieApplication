package mm.kso.movieapplication.ui.movies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;
import mm.kso.movieapplication.utils.Constants;
import mm.kso.movieapplication.adapters.CategoryMoviesAdapter;
import mm.kso.movieapplication.databinding.MoviesLayoutBinding;
import mm.kso.movieapplication.model.Movie;
import mm.kso.movieapplication.viewmodels.HomeViewModel;

@AndroidEntryPoint
public class MoviesFragment extends Fragment {

    private MoviesLayoutBinding binding;
    private HomeViewModel viewModel;
    private HashMap<String, String> map;
    private String moviesCategory="";
    private CategoryMoviesAdapter adapter;
    private ArrayList<Movie> moviesList;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MoviesLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        map = new HashMap<>();
        MoviesFragmentArgs args = MoviesFragmentArgs.fromBundle(getArguments());
        moviesCategory = args.getMovieCategory();

        map.put("page","1");

        initRecyclerView();
        observeData();
        getMoviesList();
    }

    private void initRecyclerView() {
        binding.moviesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        adapter = new CategoryMoviesAdapter(getContext(),moviesList);
        binding.moviesRecyclerView.setAdapter(adapter);
    }

    private void observeData(){
        switch (moviesCategory){
            case Constants.Current:
                viewModel.getCurrentlyShowingList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
                    @Override
                    public void onChanged(ArrayList<Movie> movies) {

                        adapter.setMovieList(movies);
                    }
                });
                break;
            case Constants.Popular:
                viewModel.getPopularMoviesList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
                    @Override
                    public void onChanged(ArrayList<Movie> movies) {
                        adapter.setMovieList(movies);
                    }
                });
                break;
            case Constants.Upcoming:
                viewModel.getUpcomingMoviesList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
                    @Override
                    public void onChanged(ArrayList<Movie> movies) {
                        adapter.setMovieList(movies);
                    }
                });
                break;
            case Constants.TopRated:
                viewModel.getTopRatedMoviesList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
                    @Override
                    public void onChanged(ArrayList<Movie> movies) {
                        adapter.setMovieList(movies);
                    }
                });
                break;
        }
    }

    private void getMoviesList(){
        switch (moviesCategory){
            case Constants.Current:
                viewModel.getCurrentlyShowingMovies(map);
                binding.moviesCategoryTitle.setText(Constants.Current);
                break;
            case Constants.Upcoming:
                viewModel.getUpcomingMovies(map);
                binding.moviesCategoryTitle.setText(Constants.Upcoming);
                break;
            case Constants.TopRated:
                viewModel.getTopRatedMovies(map);
                binding.moviesCategoryTitle.setText(Constants.TopRated);
                break;

            case Constants.Popular:
                viewModel.getPopularMovies(map);
                binding.moviesCategoryTitle.setText(Constants.Popular);
                break;
        }
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