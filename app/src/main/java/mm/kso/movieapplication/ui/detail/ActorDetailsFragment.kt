package mm.kso.movieapplication.ui.detail

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import mm.kso.movieapplication.adapters.KnownForMoviesAdapter
import mm.kso.movieapplication.databinding.FragmentActorDetailsBinding
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.utils.Constants
import mm.kso.movieapplication.viewmodels.ActorDetailslViewModel
import java.util.*

@AndroidEntryPoint
class ActorDetailsFragment : Fragment() {

    private var binding: FragmentActorDetailsBinding? = null
    private lateinit var viewModel: ActorDetailslViewModel
    private var personID: Int? = null
    private var queries: HashMap<String?, String?>? = null
    private var adapter: KnownForMoviesAdapter? = null
    private var popularMovies: ArrayList<Movie>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActorDetailsBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModel = ViewModelProvider(this).get(ActorDetailslViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val args = ActorDetailsFragmentArgs.fromBundle(
            arguments as Bundle
        )
        personID = args.personId
        queries = HashMap()
        //queries.put("api_key", BuildConfig.MOVIE_API_KEY);
        queries!!["append_to_response"] = "movie_credits"
        viewModel.getActorDetails(personID!!, queries)
        viewModel.actor.observe(viewLifecycleOwner, { actor ->
            binding?.actorName?.text = actor.name
            binding?.actorBirthday?.text = actor.birthday
            binding?.actorBio?.text = actor.biography
            binding?.actorPlace?.text = actor.place_of_birth
            Glide.with(requireContext()).load(Constants.ImageBaseURL + actor.profile_path)
                .into(binding!!.actorImage)
            binding?.actorPopularity?.text = actor.popularity.toString() + ""
            binding?.actorBioText?.visibility = View.VISIBLE
            binding?.knownForText?.visibility = View.VISIBLE
            binding?.popularityIcon?.visibility = View.VISIBLE
            val array = actor.movie_credits.getAsJsonArray("cast")
            popularMovies =
                Gson().fromJson(array.toString(), object : TypeToken<ArrayList<Movie?>?>() {}.type)
            initKnownFor(popularMovies)
        })
        binding!!.knownForRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun initKnownFor(movies: ArrayList<Movie>?) {
        Log.e(TAG, "initKnownFor: " + movies!!.size)
        binding?.knownForRecyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        adapter = KnownForMoviesAdapter(requireContext(), movies)
        binding?.knownForRecyclerView?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            requireActivity().onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "ActorDetails"
    }
}