package mm.kso.movieapplication.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mm.kso.movieapplication.adapters.SearchAdapter
import mm.kso.movieapplication.databinding.FragmentSearchBinding
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.viewmodels.SearchViewModel
import java.util.*

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding? = null
    private lateinit var viewModel: SearchViewModel
    private var queryMap: HashMap<String?, String?>? = null
    private var adapter: SearchAdapter? = null
    private val moviesList: ArrayList<Movie>? = null
    private var queryText = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryMap = HashMap()
        queryMap!!["query"] = queryText
        initRecyclerView()
        observeData()
        viewModel.getQueriedMovies(queryMap)
        binding!!.searchMovie.setOnClickListener {
            queryText = binding!!.searchKeyword.text.toString().trim { it <= ' ' }.toLowerCase()
            queryMap?.clear()
            //queryMap.put("api_key", BuildConfig.MOVIE_API_KEY);
            queryMap!!["query"] = queryText
            viewModel.getQueriedMovies(queryMap)
        }
        binding!!.searchKeyword.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                queryText = binding!!.searchKeyword.text.toString().trim { it <= ' ' }.toLowerCase()
                queryMap!!.clear()
                //queryMap.put("api_key", BuildConfig.MOVIE_API_KEY);
                queryMap!!["query"] = queryText
                viewModel!!.getQueriedMovies(queryMap)
            }
            false
        }
    }

    private fun initRecyclerView() {
        binding!!.searchMoviesRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        adapter = SearchAdapter(requireContext(), moviesList)
        binding!!.searchMoviesRecyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel!!.queriesMovies.observe(
            viewLifecycleOwner,
            Observer<ArrayList<Movie>> { movies -> adapter!!.setMoviesList(movies) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}