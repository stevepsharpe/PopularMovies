package pe.shar.popularmovies.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.shar.popularmovies.R;
import pe.shar.popularmovies.data.Movie;
import pe.shar.popularmovies.data.Video;

/**
 * Created by steve on 04/04/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private List<Video> mVideos;
    private Context context;

    private final VideosAdapter.VideoAdapterOnClickHandler mClickHandler;

    public interface VideoAdapterOnClickHandler {
        void onClick(Video video);
    }

    public VideosAdapter(Context context, List<Video> videos, VideosAdapter.VideoAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.mVideos = videos;
        this.mClickHandler = clickHandler;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final String TAG = VideoViewHolder.class.getSimpleName();

        @BindView(R.id.video_thumb)
        ImageView videoThumb;

        @BindView(R.id.video_name)
        TextView videoName;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            Video video = mVideos.get(getAdapterPosition());
            mClickHandler.onClick(video);
        }
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        return new VideosAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Video video = mVideos.get(position);

        holder.videoName.setText(video.getName());

        String youtubeThumbUrl = "https://img.youtube.com/vi/%s/maxresdefault.jpg";

        Glide.with(context)
                .load(String.format(youtubeThumbUrl, video.getKey()))
                .placeholder(new ColorDrawable(context.getResources().getColor(R.color.colorPrimary)))
                .diskCacheStrategy(DiskCacheStrategy.ALL) // caches all versions of the image
                .crossFade()
                .fitCenter()
                .into(holder.videoThumb);
    }

    @Override
    public int getItemCount() {
        if (null == mVideos) return 0;
        return mVideos.size();
    }

    public Video getShareableVideo() {
        if (null == mVideos || mVideos.size() == 0) return null;
        return mVideos.get(0);
    }

    public void setVideos(List<Video> videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }
}
