package mm.kso.movieapplication.ui.detail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Video
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import mm.kso.movieapplication.R
import mm.kso.movieapplication.adapters.CastAdapter
import mm.kso.movieapplication.databinding.FragmentMovieDetailsBinding
import mm.kso.movieapplication.db.FavoriteMovie
import mm.kso.movieapplication.model.Cast
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.ui.detail.MovieDetailsFragment
import mm.kso.movieapplication.utils.Constants
import mm.kso.movieapplication.viewmodels.MovieDetailsViewModel
import java.util.*

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    
    private var binding: FragmentMovieDetailsBinding? = null
    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var queryMap: HashMap<String, String>
    private lateinit var temp: String
    private lateinit var videoId: String
    private lateinit var adapter: CastAdapter
    private lateinit var castList: ArrayList<Cast>
    private var hour = 0
    private var min = 0
    private lateinit var mMovie: Movie
    private var inFavList = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModel = ViewModelProvider(this@MovieDetailsFragment).get(
            MovieDetailsViewModel::class.java
        )
        return view
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        castList = ArrayList()
        queryMap = HashMap()
        val args = MovieDetailsFragmentArgs.fromBundle(
            arguments as Bundle
        )
        val movieId: Int = args.movieId
        observeData(movieId)
        queryMap["page"] = "1"
        queryMap["append_to_response"] = "videos"
        viewModel.getMovieDetails(movieId, queryMap)
        viewModel.getCast(movieId, queryMap)
        binding?.castRecyclerView?.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        adapter = CastAdapter(requireContext(), castList)
        binding?.castRecyclerView?.adapter = adapter
        binding?.moviePoster?.clipToOutline = true
        binding?.addToFavoriteList?.setOnClickListener {
            if (inFavList) {
                viewModel.deleteMovie(movieId)
                binding?.addToFavoriteList?.setImageResource(R.drawable.ic_playlist)
                Toast.makeText(context, "Removed from Favorite List.", Toast.LENGTH_SHORT).show()
            } else {
                val movie = FavoriteMovie(
                    mMovie.id,
                    mMovie.poster_path,
                    mMovie.overview,
                    mMovie.release_date,
                    mMovie.title,
                    mMovie.backdrop_path,
                    mMovie.vote_count,
                    mMovie.runtime
                )
                viewModel.insertMovie(movie)
                binding?.addToFavoriteList?.setImageResource(R.drawable.ic_playlist_add)
                Toast.makeText(context, "Added to Favorite List.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isMovieInFavList(movieId: Int) {
        inFavList = if (viewModel.getFavoriteListMovie(movieId) != null) {
            binding?.addToFavoriteList?.setImageResource(R.drawable.ic_playlist_add)
            true
        } else {
            binding?.addToFavoriteList?.setImageResource(R.drawable.ic_playlist)
            false
        }
        binding?.addToFavoriteList?.visibility = View.VISIBLE
    }

    private fun observeData(movieId: Int) {
        viewModel.movie.observe(viewLifecycleOwner, { movie ->
            mMovie = movie
            Glide.with(requireContext()).load(Constants.ImageBaseURL + movie.poster_path)
                .centerCrop()
                .into(binding?.moviePoster!!)
            binding?.movieName?.text = movie.title
            hour = movie.runtime / 60
            min = movie.runtime % 60
            binding?.movieRuntime?.text = "$hour h  $min m"
            binding?.moviePlot?.text = movie.overview
            temp = ""
            for (i in movie.genres.indices) {
                temp += if (i == movie.genres.size - 1) movie.genres[i].name else movie.genres[i].name + " • "
            }
            binding?.movieGenre?.text = temp
            binding?.movieCastText?.visibility = View.VISIBLE
            binding?.moviePlotText?.visibility = View.VISIBLE
            isMovieInFavList(movieId)
            val array = movie.videos.getAsJsonArray("results")
            videoId = array[0].asJsonObject["key"].asString
        })
        viewModel.movieCastList.observe(viewLifecycleOwner, Observer<ArrayList<Cast>> { actors ->
            Log.e(TAG, "onChanged: " + actors.size)
            adapter.setCastList(actors)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "MovieDetails"
    }
}