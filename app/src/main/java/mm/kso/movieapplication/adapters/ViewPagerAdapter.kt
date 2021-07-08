package mm.kso.movieapplication.adapters

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mm.kso.movieapplication.adapters.ViewPagerAdapter.ViewPagerViewHolder
import mm.kso.movieapplication.databinding.CuttentlyShowItemBinding
import mm.kso.movieapplication.model.Movie
import mm.kso.movieapplication.ui.home.HomeFragmentDirections
import mm.kso.movieapplication.utils.Constants
import java.util.*

class ViewPagerAdapter(private val context: Context, private var movieList: ArrayList<Movie>?) :
    RecyclerView.Adapter<ViewPagerViewHolder>() {

    private var binding: CuttentlyShowItemBinding? = null

    fun setMovieListResults(movieList: ArrayList<Movie>?) {
        this.movieList = movieList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val inflater = LayoutInflater.from(context)
        binding = CuttentlyShowItemBinding.inflate(inflater, parent, false)
        return ViewPagerViewHolder(binding!!)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        Log.e(TAG, "onBindViewHolder: " + movieList!![position].title)
        holder.binding.currentlyShowingMovieName.text = movieList!![position].title
        Glide.with(context).load(Constants.ImageBaseURL + movieList!![position].backdrop_path)
            .into(holder.binding.currentlyShowingMovieImage)
        holder.binding.currentlyShowingLayout.setOnClickListener { view ->
            val action = HomeFragmentDirections.actionHomeToMovieDetails(
                movieList!![position].id
            )
            Navigation.findNavController(view).navigate(action)
        }
        holder.binding.currentlyShowingMovieImage.clipToOutline = true
        holder.binding.currentlyShowingLayout.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun getItemCount(): Int {
        return if (movieList == null) 0 else movieList!!.size
    }

    inner class ViewPagerViewHolder(val binding: CuttentlyShowItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

    companion object {
        private const val TAG = "ViewPagerAdapter"
    }
}