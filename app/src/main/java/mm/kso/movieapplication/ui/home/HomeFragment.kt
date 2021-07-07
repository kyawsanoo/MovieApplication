package mm.kso.movieapplication.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;
import mm.kso.movieapplication.BuildConfig;
import mm.kso.movieapplication.utils.Constants;
import mm.kso.movieapplication.adapters.HomeAdapter;
import mm.kso.movieapplication.adapters.ViewPagerAdapter;
import mm.kso.movieapplication.databinding.FragmentHomeBinding;
import mm.kso.movieapplication.model.Movie;
import mm.kso.movieapplication.viewmodels.HomeViewModel;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ViewPagerAdapter currentMoviesAdapter;
    private HomeAdapter upcomingAdapter, popularAdapter, topRatedAdapter;

    private ArrayList<Movie> currentMovies, popularMovies, topRatedMovies, upcomingMovies;
    private HashMap<String, String> map = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        homeViewModel = new ViewModelProvider(HomeFragment.this).get(HomeViewModel.class);
        View view = binding.getRoot();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.progressBar.setVisibility(View.VISIBLE);
        map.put("page", "1");
        observeData();
        setUpRecyclerViewsAndViewPager();
        setUpOnclick();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStart() {
        super.onStart();
        if(Constants.isNetworkAvailable(requireContext())){
            getMoviesList();
        }
    }

    private void setUpRecyclerViewsAndViewPager() {
        currentMoviesAdapter = new ViewPagerAdapter(getContext(), currentMovies);
        binding.currentlyShowingViewPager.setAdapter(currentMoviesAdapter);

        binding.upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        upcomingAdapter = new HomeAdapter(getContext(), upcomingMovies);
        binding.upcomingRecyclerView.setAdapter(upcomingAdapter);

        binding.topRatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        topRatedAdapter = new HomeAdapter(getContext(), topRatedMovies);
        binding.topRatedRecyclerView.setAdapter(topRatedAdapter);

        binding.popularRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        popularAdapter = new HomeAdapter(getContext(), popularMovies);
        binding.popularRecyclerView.setAdapter(popularAdapter);
    }

    private void observeData() {
        homeViewModel.getCurrentlyShowingList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                if (movies.size() == 0 || movies == null){
                    binding.progressBar.setVisibility(View.VISIBLE);
                }
                else{
                    binding.currentlyShowing.setVisibility(View.VISIBLE);
                    binding.viewAllCurrent.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                    currentMoviesAdapter.setMovieListResults(movies);
                }
            }
        });
        homeViewModel.getPopularMoviesList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                binding.popular.setVisibility(View.VISIBLE);
                binding.viewAllPopular.setVisibility(View.VISIBLE);
                popularAdapter.setMoviesList(movies);
            }
        });

        homeViewModel.getUpcomingMoviesList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                binding.upcomingShowing.setVisibility(View.VISIBLE);
                binding.viewAllUpcoming.setVisibility(View.VISIBLE);
                upcomingAdapter.setMoviesList(movies);
            }
        });


        homeViewModel.getTopRatedMoviesList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                binding.topRated.setVisibility(View.VISIBLE);
                binding.viewAllTopRated.setVisibility(View.VISIBLE);
                topRatedAdapter.setMoviesList(movies);
            }
        });
    }

    private void getMoviesList() {
        homeViewModel.getCurrentlyShowingMovies(map);
        homeViewModel.getPopularMovies(map);
        homeViewModel.getUpcomingMovies(map);
        homeViewModel.getTopRatedMovies(map);
    }

    private void setUpOnclick() {
        binding.viewAllCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionHomeToMovies action = HomeFragmentDirections.actionHomeToMovies();
                action.setMovieCategory(Constants.Current);
                Navigation.findNavController(v).navigate(action);
            }
        });

        binding.viewAllUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionHomeToMovies action = HomeFragmentDirections.
                        actionHomeToMovies();
                action.setMovieCategory(Constants.Upcoming);
                Navigation.findNavController(v).navigate(action);
            }
        });

        binding.viewAllTopRated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionHomeToMovies action =
                        HomeFragmentDirections.actionHomeToMovies();
                action.setMovieCategory(Constants.TopRated);
                Navigation.findNavController(v).navigate(action);
            }
        });

        binding.viewAllPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionHomeToMovies action =
                        HomeFragmentDirections.actionHomeToMovies();
                action.setMovieCategory(Constants.Popular);
                Navigation.findNavController(v).navigate(action);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}