package mm.kso.movieapplication.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mm.kso.movieapplication.adapters.FavoriteAdapter
import mm.kso.movieapplication.databinding.FragmentFavoriteBinding
import mm.kso.movieapplication.db.FavoriteMovie
import mm.kso.movieapplication.viewmodels.FavoriteViewModel

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    
    private lateinit var viewModel: FavoriteViewModel
    private var binding: FragmentFavoriteBinding? = null
    private var adapter: FavoriteAdapter? = null
    private var moviesList: MutableList<FavoriteMovie>? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val view = binding?.root
        viewModel = ViewModelProvider(this@FavoriteFragment).get(
            FavoriteViewModel::class.java
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiRecyclerView()
        observeData()
        binding?.clearFavList?.setOnClickListener {
            viewModel.clearWishList()
            Toast.makeText(context, "Favorite List Cleared!", Toast.LENGTH_SHORT).show()
            moviesList!!.clear()
            adapter!!.setMoviesList(moviesList)
        }
    }

    private fun observeData() {
        viewModel.favoriteMoviesList.observe(
            viewLifecycleOwner,
            Observer<List<FavoriteMovie>>{ favoriteMovies ->
                if (favoriteMovies.size == 0) {
                    binding!!.placeHolderText.visibility = View.VISIBLE
                    binding!!.clearFavList.visibility = View.GONE
                } else {
                    binding!!.placeHolderText.visibility = View.GONE
                    binding!!.clearFavList.visibility = View.VISIBLE
                    adapter!!.setMoviesList(favoriteMovies)
                    moviesList = favoriteMovies as MutableList<FavoriteMovie>?
                }
            }
        )
    }

    private fun intiRecyclerView() {
        binding!!.favListRecyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = FavoriteAdapter(requireContext(), moviesList)
        binding!!.favListRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}