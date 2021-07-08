package mm.kso.movieapplication.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mm.kso.movieapplication.adapters.SearchAdapter.SearchViewHolder
import mm.kso.movieapplication.databinding.MovieItemBinding
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.ui.search.SearchFragmentDirections
import mm.kso.movieapplication.utils.Constants
import mm.kso.movieapplication.utils.Constants.genreMap
import java.util.*

class SearchAdapter(private val context: Context, private var moviesList: ArrayList<Movie>?) :
    RecyclerView.Adapter<SearchViewHolder>() {

    private var binding: MovieItemBinding? = null

    private var temp: String? = null

    fun setMoviesList(moviesList: ArrayList<Movie>?) {
        this.moviesList = moviesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(context)
        binding = MovieItemBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding!!)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.binding.movieName.text = moviesList!![position].title
        temp = ""
        for (i in moviesList!![position].genre_ids.indices) {
            temp += if (i == moviesList!![position].genre_ids.size - 1) genreMap[moviesList!![position].genre_ids[i]] else genreMap[moviesList!![position].genre_ids[i]].toString() + " • "
        }
        holder.binding.movieGenre.text = temp
        holder.binding.movieRating.rating = moviesList!![position].vote_average.toFloat() / 2
        val movieYear = moviesList!![position].release_date
            .split("-").toTypedArray()
        holder.binding.movieYear.text = movieYear[0]
        Glide.with(context).load(Constants.ImageBaseURL + moviesList!![position].poster_path)
            .into(holder.binding.movieImage)
        holder.binding.movieItemLayout.setOnClickListener { view ->
            val action = SearchFragmentDirections.actionSearchMoviesToMovieDetails(
                moviesList!![position].id
            )
            Navigation.findNavController(view).navigate(action)
        }
        holder.binding.movieItemLayout.clipToOutline = true
    }

    override fun getItemCount(): Int {
        return if (moviesList == null) 0 else moviesList!!.size
    }

    inner class SearchViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}