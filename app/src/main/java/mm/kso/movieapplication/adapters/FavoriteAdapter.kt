package mm.kso.movieapplication.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mm.kso.movieapplication.adapters.FavoriteAdapter.FavoriteViewHolder
import mm.kso.movieapplication.databinding.HomeItemBinding
import mm.kso.movieapplication.db.FavoriteMovie
import mm.kso.movieapplication.ui.favourite.FavoriteFragmentDirections
import mm.kso.movieapplication.utils.Constants

class FavoriteAdapter(private val context: Context, private var moviesList: List<FavoriteMovie>?) :
    RecyclerView.Adapter<FavoriteViewHolder>() {
    private var binding: HomeItemBinding? = null
    fun setMoviesList(moviesList: List<FavoriteMovie>?) {
        this.moviesList = moviesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = LayoutInflater.from(context)
        binding = HomeItemBinding.inflate(inflater, parent, false)
        return FavoriteViewHolder(binding!!)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.binding.movieItemRelativeLayout.clipToOutline = true
        holder.binding.movieItemName.text = moviesList!![position].title
        holder.binding.movieItemVotes.text = moviesList!![position].vote_count.toString() + ""
        Glide.with(context).load(Constants.ImageBaseURLw500 + moviesList!![position].poster_path)
            .into(holder.binding.movieItemImage)
        holder.binding.movieItemRelativeLayout.setOnClickListener { view ->
            val action = FavoriteFragmentDirections.actionFavoriteToMovieDetails(
                moviesList!![position].id
            )
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return if (moviesList == null) 0 else moviesList!!.size
    }

    inner class FavoriteViewHolder(val binding: HomeItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    companion object {
        private const val TAG = "WishListAdapter"
    }
}