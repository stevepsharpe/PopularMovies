package pe.shar.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.shar.popularmovies.R;
import pe.shar.popularmovies.data.Review;

/**
 * Created by steve on 05/04/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<Review> mReviews;
    private Context context;

    public ReviewsAdapter(Context context, List<Review> mReviews) {
        this.context = context;
        this.mReviews = mReviews;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = ReviewsAdapter.ReviewViewHolder.class.getSimpleName();

        @BindView(R.id.review_author)
        TextView reviewAuthor;

        @BindView(R.id.review_content)
        TextView reviewContent;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);
        return new ReviewsAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);

        String authorString = "A review by %s";
        holder.reviewAuthor.setText(String.format(authorString, review.getAuthor()));
        holder.reviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        return mReviews.size();
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }
}
