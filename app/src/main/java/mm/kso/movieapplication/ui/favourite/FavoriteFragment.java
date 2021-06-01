package mm.kso.movieapplication.ui.favourite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import mm.kso.movieapplication.adapters.FavoriteAdapter;
import mm.kso.movieapplication.databinding.FragmentFavoriteBinding;
import mm.kso.movieapplication.db.FavoriteMovie;
import mm.kso.movieapplication.viewmodels.FavoriteViewModel;

@AndroidEntryPoint
public class FavoriteFragment extends Fragment {


    private FavoriteViewModel viewModel;
    private FragmentFavoriteBinding binding;
    private FavoriteAdapter adapter;
    private List<FavoriteMovie> moviesList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(FavoriteFragment.this).get(FavoriteViewModel.class);
        return (view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        intiRecyclerView();
        observeData();

        binding.clearFavList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.clearWishList();
                Toast.makeText(getContext(),"Favorite List Cleared!",Toast.LENGTH_SHORT).show();
                moviesList.clear();
                adapter.setMoviesList(moviesList);
            }
        });
    }

    private void observeData() {
        viewModel.getFavoriteMoviesList().observe(getViewLifecycleOwner(), new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                if (favoriteMovies.size() == 0 || favoriteMovies == null){
                    binding.placeHolderText.setVisibility(View.VISIBLE);
                    binding.clearFavList.setVisibility(View.GONE);
                }
                else{
                    binding.placeHolderText.setVisibility(View.GONE);
                    binding.clearFavList.setVisibility(View.VISIBLE);
                    adapter.setMoviesList(favoriteMovies);
                    moviesList = favoriteMovies;
                }
            }
        });
    }

    private void intiRecyclerView() {
        binding.favListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapter  = new FavoriteAdapter(getContext(),moviesList);
        binding.favListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}