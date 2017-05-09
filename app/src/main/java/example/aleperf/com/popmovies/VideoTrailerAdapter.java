package example.aleperf.com.popmovies;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;


import example.aleperf.com.popmovies.databinding.VideoTrailerItemBinding;
import example.aleperf.com.popmovies.utilities.NetworkUtils;

import static android.content.Intent.ACTION_VIEW;


public class VideoTrailerAdapter extends RecyclerView.Adapter<VideoTrailerAdapter.VideoTrailerHolder> {

    private List<VideoTrailer> listOfVideos;
    private Context mContext;
    private VideoTrailerItemBinding mBinding;

    public VideoTrailerAdapter(Context context){
        mContext = context;
    }

    public VideoTrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(mContext);
       mBinding  = DataBindingUtil.inflate(layoutInflater, R.layout.video_trailer_item, parent, false);
        return new VideoTrailerHolder(mBinding);
    }

    public void onBindViewHolder(VideoTrailerHolder holder,
                                 int position) {
        VideoTrailer trailer = listOfVideos.get(position);
        holder.bindVideoTrailer(trailer);
    }

    @Override
    public int getItemCount() {
        if (listOfVideos == null) {
            return 0;
        }
        return listOfVideos.size();
    }

    public void setVideoTrailerData(List<VideoTrailer> trailers) {
        listOfVideos = trailers;
        notifyDataSetChanged();
    }

    public class VideoTrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final VideoTrailerItemBinding binding;


        public VideoTrailerHolder(VideoTrailerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        private void bindVideoTrailer(VideoTrailer trailer) {

            binding.setVideo(trailer);
            itemView.setOnClickListener(this);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            String trailerId = binding.getVideo().getTrailerId();
            Uri youtubeVideoUri = NetworkUtils.buildYoutubeUri(trailerId);
            Intent intent = new Intent(ACTION_VIEW, youtubeVideoUri);

            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
               mContext.startActivity(intent);
            }

        }
    }
}
