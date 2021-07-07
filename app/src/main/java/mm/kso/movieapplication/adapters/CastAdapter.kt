package mm.kso.movieapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import mm.kso.movieapplication.adapters.CastAdapter.CastViewModel
import mm.kso.movieapplication.databinding.CastItemBinding
import mm.kso.movieapplication.model.Cast
import mm.kso.movieapplication.ui.detail.MovieDetailsFragmentDirections
import mm.kso.movieapplication.utils.Constants
import java.util.*

class CastAdapter(private val context: Context, private var castList: ArrayList<Cast>?) :
    RecyclerView.Adapter<CastViewModel>() {
    private var binding: CastItemBinding? = null
    fun setCastList(castList: ArrayList<Cast>?) {
        this.castList = castList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewModel {
        val inflater = LayoutInflater.from(context)
        binding = CastItemBinding.inflate(inflater, parent, false)
        return CastViewModel(binding!!)
    }

    override fun onBindViewHolder(holder: CastViewModel, position: Int) {
        holder.binding.castName.text = castList!![position].name
        holder.binding.castRole.text = castList!![position].character
        Glide.with(context).load(Constants.ImageBaseURL + castList!![position].profile_path)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.binding.castImage)
        holder.binding.castItemLayout.setOnClickListener { view ->
            val action = MovieDetailsFragmentDirections.actionMovieDetailsToActorDetails(
                castList!![position].id
            )
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return if (castList == null) 0 else castList!!.size
    }

    inner class CastViewModel(val binding: CastItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}