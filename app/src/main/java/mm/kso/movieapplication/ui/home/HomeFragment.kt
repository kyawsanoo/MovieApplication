package mm.kso.movieapplication.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import mm.kso.movieapplication.adapters.HomeAdapter
import mm.kso.movieapplication.adapters.ViewPagerAdapter
import mm.kso.movieapplication.databinding.FragmentHomeBinding
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.utils.Constants
import mm.kso.movieapplication.utils.Constants.isNetworkAvailable
import mm.kso.movieapplication.viewmodels.HomeViewModel
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var binding: FragmentHomeBinding? = null
    private var currentMoviesAdapter: ViewPagerAdapter? = null
    private var upcomingAdapter: HomeAdapter? = null
    private var popularAdapter: HomeAdapter? = null
    private var topRatedAdapter: HomeAdapter? = null
    private val currentMovies: ArrayList<Movie>? = null
    private val popularMovies: ArrayList<Movie>? = null
    private val topRatedMovies: ArrayList<Movie>? = null
    private val upcomingMovies: ArrayList<Movie>? = null
    private val map = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        homeViewModel =
            ViewModelProvider(this@HomeFragment).get(HomeViewModel::class.java)
        return binding?.root!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.progressBar?.visibility = View.VISIBLE
        map["page"] = "1"
        observeData()
        setUpRecyclerViewsAndViewPager()
        setUpOnclick()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        super.onStart()
        if (isNetworkAvailable(requireContext())) {
            moviesList
        }
    }

    private fun setUpRecyclerViewsAndViewPager() {
        currentMoviesAdapter = ViewPagerAdapter(requireContext(), currentMovies)
        binding?.currentlyShowingViewPager?.adapter = currentMoviesAdapter
        binding?.upcomingRecyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        upcomingAdapter = HomeAdapter(requireContext(), upcomingMovies)
        binding?.upcomingRecyclerView?.adapter = upcomingAdapter
        binding?.topRatedRecyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        topRatedAdapter = HomeAdapter(requireContext(), topRatedMovies)
        binding?.topRatedRecyclerView?.adapter = topRatedAdapter
        binding?.popularRecyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        popularAdapter = HomeAdapter(requireContext(), popularMovies)
        binding?.popularRecyclerView?.adapter = popularAdapter
    }

    private fun observeData() {
        homeViewModel.currentlyShowingList.observe(viewLifecycleOwner, { movies ->
            if (movies?.size == 0 || movies == null) {
                binding?.progressBar?.visibility = View.VISIBLE
            } else {
                binding?.currentlyShowing?.visibility = View.VISIBLE
                binding?.viewAllCurrent?.visibility = View.VISIBLE
                binding?.progressBar?.visibility = View.GONE
                currentMoviesAdapter?.setMovieListResults(movies)
            }
        })
        homeViewModel.popularMoviesList.observe(viewLifecycleOwner, { movies ->
            binding?.popular?.visibility = View.VISIBLE
            binding?.viewAllPopular?.visibility = View.VISIBLE
            popularAdapter?.setMoviesList(movies)
        })
        homeViewModel.upcomingMoviesList.observe(viewLifecycleOwner, { movies ->
            binding?.upcomingShowing?.visibility = View.VISIBLE
            binding?.viewAllUpcoming?.visibility = View.VISIBLE
            upcomingAdapter?.setMoviesList(movies)
        })
        homeViewModel.topRatedMoviesList.observe(viewLifecycleOwner, { movies ->
            binding?.topRated?.visibility = View.VISIBLE
            binding?.viewAllTopRated?.visibility = View.VISIBLE
            topRatedAdapter?.setMoviesList(movies)
        })
    }

    private val moviesList: Unit
        private get() {
            homeViewModel.getCurrentlyShowingMovies(map)
            homeViewModel.getPopularMovies(map)
            homeViewModel.getUpcomingMovies(map)
            homeViewModel.getTopRatedMovies(map)
        }

    private fun setUpOnclick() {
        binding?.viewAllCurrent?.setOnClickListener { v ->
            val action = HomeFragmentDirections.actionHomeToMovies()
            action.movieCategory = Constants.Current
            Navigation.findNavController(v).navigate(action)
        }
        binding?.viewAllUpcoming?.setOnClickListener { v ->
            val action = HomeFragmentDirections.actionHomeToMovies()
            action.movieCategory = Constants.Upcoming
            Navigation.findNavController(v).navigate(action)
        }
        binding?.viewAllTopRated?.setOnClickListener { v ->
            val action = HomeFragmentDirections.actionHomeToMovies()
            action.movieCategory = Constants.TopRated
            Navigation.findNavController(v).navigate(action)
        }
        binding?.viewAllPopular?.setOnClickListener { v ->
            val action = HomeFragmentDirections.actionHomeToMovies()
            action.movieCategory = Constants.Popular
            Navigation.findNavController(v).navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}