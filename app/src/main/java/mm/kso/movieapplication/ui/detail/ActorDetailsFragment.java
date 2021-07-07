package mm.kso.movieapplication.ui.detail;

import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;
import mm.kso.movieapplication.BuildConfig;
import mm.kso.movieapplication.utils.Constants;
import mm.kso.movieapplication.adapters.KnownForMoviesAdapter;
import mm.kso.movieapplication.databinding.FragmentActorDetailsBinding;
import mm.kso.movieapplication.model.Actor;
import mm.kso.movieapplication.model.Movie;
import mm.kso.movieapplication.viewmodels.ActorDetailslViewModel;

@AndroidEntryPoint
public class ActorDetailsFragment extends Fragment {
    private static final String TAG = "ActorDetails";
    private FragmentActorDetailsBinding binding;
    private ActorDetailslViewModel viewModel;
    private Integer personID;
    private HashMap<String, String> queries;
    private KnownForMoviesAdapter adapter;
    private ArrayList<Movie> popularMovies;

    public ActorDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentActorDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(ActorDetailslViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        ActorDetailsFragmentArgs args = ActorDetailsFragmentArgs.fromBundle(getArguments());
        personID = args.getPersonId();

        queries = new HashMap<>();
        //queries.put("api_key", BuildConfig.MOVIE_API_KEY);
        queries.put("append_to_response", "movie_credits");

        viewModel.getActorDetails(personID, queries);

        viewModel.getActor().observe(getViewLifecycleOwner(), new Observer<Actor>() {
            @Override
            public void onChanged(Actor actor) {

                binding.actorName.setText(actor.getName());
                binding.actorBirthday.setText(actor.getBirthday());
                binding.actorBio.setText(actor.getBiography());
                binding.actorPlace.setText(actor.getPlace_of_birth());
                Glide.with(getContext()).load(Constants.ImageBaseURL + actor.getProfile_path())
                        .into(binding.actorImage);
                binding.actorPopularity.setText(actor.getPopularity() + "");
                binding.actorBioText.setVisibility(View.VISIBLE);
                binding.knownForText.setVisibility(View.VISIBLE);
                binding.popularityIcon.setVisibility(View.VISIBLE);
                JsonArray array = actor.getMovie_credits().getAsJsonArray("cast");
                popularMovies = new Gson().fromJson(array.toString(), new TypeToken<ArrayList<Movie>>() {}.getType());
                initKnownFor(popularMovies);

            }
        });
        binding.knownForRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    private void initKnownFor(ArrayList<Movie> movies) {
        Log.e(TAG, "initKnownFor: "+ movies.size() );
        binding.knownForRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        adapter = new KnownForMoviesAdapter(getContext(),movies);
        binding.knownForRecyclerView.setAdapter(adapter);
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