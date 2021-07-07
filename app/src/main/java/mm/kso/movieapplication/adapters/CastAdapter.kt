package mm.kso.movieapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;

import mm.kso.movieapplication.utils.Constants;
import mm.kso.movieapplication.databinding.CastItemBinding;
import mm.kso.movieapplication.model.Cast;
import mm.kso.movieapplication.ui.detail.MovieDetailsFragmentDirections;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewModel> {
    private CastItemBinding binding;
    private Context context;
    private ArrayList<Cast> castList;

    public CastAdapter(Context context, ArrayList<Cast> castList) {
        this.context = context;
        this.castList = castList;
    }

    public void setCastList(ArrayList<Cast> castList) {
        this.castList = castList;
    }

    @NonNull
    @Override
    public CastViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = CastItemBinding.inflate(inflater, parent, false);
        return new CastViewModel(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewModel holder, int position) {
        holder.binding.castName.setText(castList.get(position).getName());
        holder.binding.castRole.setText(castList.get(position).getCharacter());
        Glide.with(context).load(Constants.ImageBaseURL + castList.get(position).getProfile_path())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.binding.castImage);

        holder.binding.castItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieDetailsFragmentDirections.ActionMovieDetailsToActorDetails action  =
                        MovieDetailsFragmentDirections.actionMovieDetailsToActorDetails(castList.get(position).getId());
                Navigation.findNavController(view).navigate(action);
            }
        });

    }

    @Override
    public int getItemCount() {
        return castList == null ? 0 : castList.size();
    }

    class CastViewModel extends RecyclerView.ViewHolder {

        private CastItemBinding binding;
        public CastViewModel(CastItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

