package mm.kso.movieapplication.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mm.kso.movieapplication.adapters.KnownForMoviesAdapter.KnownForMoviesViewHolder
import mm.kso.movieapplication.databinding.HomeItemBinding
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.ui.detail.ActorDetailsFragmentDirections
import mm.kso.movieapplication.utils.Constants
import java.util.*

class KnownForMoviesAdapter(
    private val mContext: Context,
    private var moviesList: ArrayList<Movie>?
) : RecyclerView.Adapter<KnownForMoviesViewHolder>() {
    private var binding: HomeItemBinding? = null
    fun setMoviesList(moviesList: ArrayList<Movie>?) {
        this.moviesList = moviesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnownForMoviesViewHolder {
        val inflater = LayoutInflater.from(mContext)
        binding = HomeItemBinding.inflate(inflater, parent, false)
        return KnownForMoviesViewHolder(binding!!)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: KnownForMoviesViewHolder, position: Int) {
        holder.binding.movieItemRelativeLayout.clipToOutline = true
        holder.binding.movieItemName.text = moviesList!![position].title
        holder.binding.movieItemVotes.text = moviesList!![position].vote_count.toString() + ""
        Glide.with(mContext).load(Constants.ImageBaseURLw500 + moviesList!![position].poster_path)
            .into(holder.binding.movieItemImage)
        holder.binding.movieItemRelativeLayout.setOnClickListener { view ->
            val action = ActorDetailsFragmentDirections.actionActorDetailsToMovieDetails(
                moviesList!![position].id
            )
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return if (moviesList == null) 0 else moviesList!!.size
    }

    inner class KnownForMoviesViewHolder(val binding: HomeItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}