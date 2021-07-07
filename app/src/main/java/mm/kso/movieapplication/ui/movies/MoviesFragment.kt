package mm.kso.movieapplication.ui.movies

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mm.kso.movieapplication.adapters.CategoryMoviesAdapter
import mm.kso.movieapplication.databinding.MoviesLayoutBinding
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.utils.Constants
import mm.kso.movieapplication.viewmodels.HomeViewModel
import java.util.*

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var binding: MoviesLayoutBinding? = null
    private lateinit var viewModel: HomeViewModel
    private lateinit var map: HashMap<String, String>
    private var moviesCategory = ""
    private var adapter: CategoryMoviesAdapter? = null
    private var moviesList: ArrayList<Movie>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MoviesLayoutBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        map = HashMap()
        val args = MoviesFragmentArgs.fromBundle(arguments as Bundle)
        moviesCategory = args.movieCategory
        map["page"] = "1"
        initRecyclerView()
        observeData()
        getMoviesList()
    }

    private fun initRecyclerView() {
        binding!!.moviesRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        adapter = CategoryMoviesAdapter(requireContext(), moviesList)
        binding!!.moviesRecyclerView.adapter = adapter
    }

    private fun observeData() {
        when (moviesCategory) {
            Constants.Current -> viewModel.currentlyShowingList.observe(
                viewLifecycleOwner, { movies -> adapter?.setMovieList(movies) })
            Constants.Popular -> viewModel.popularMoviesList.observe(
                viewLifecycleOwner, { movies -> adapter?.setMovieList(movies) })
            Constants.Upcoming -> viewModel.upcomingMoviesList.observe(
                viewLifecycleOwner, { movies -> adapter?.setMovieList(movies) })
            Constants.TopRated -> viewModel.topRatedMoviesList.observe(
                viewLifecycleOwner, { movies -> adapter?.setMovieList(movies) })
        }
    }

    private fun getMoviesList() {
        when (moviesCategory) {
            Constants.Current -> {
                map.let { viewModel.getCurrentlyShowingMovies(it) }
                binding!!.moviesCategoryTitle.text = Constants.Current
            }
            Constants.Upcoming -> {
                map.let { viewModel.getUpcomingMovies(it) }
                binding!!.moviesCategoryTitle.text = Constants.Upcoming
            }
            Constants.TopRated -> {
                map.let { viewModel.getTopRatedMovies(it) }
                binding!!.moviesCategoryTitle.text = Constants.TopRated
            }
            Constants.Popular -> {
                map.let { viewModel.getPopularMovies(it) }
                binding!!.moviesCategoryTitle.text = Constants.Popular
            }
        }
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
}