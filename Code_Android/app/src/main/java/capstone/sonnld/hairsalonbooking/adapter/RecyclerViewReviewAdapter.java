package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.DetailSalonActivity;
import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.ModelReview;

public class RecyclerViewReviewAdapter extends RecyclerView.Adapter<RecyclerViewReviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ModelReview> modelReviews;
    private String cusName;
    private String cusAvt;
    private int rating;
    private String comment;

    public RecyclerViewReviewAdapter(Context mContext, ArrayList<ModelReview> modelReviews) {
        this.mContext = mContext;
        this.modelReviews = modelReviews;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_review, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //show item

        cusAvt = modelReviews.get(position).getAccount().getAvatar();
        cusName = modelReviews.get(position).getAccount().getFullname();
        rating = modelReviews.get(position).getRating();
        comment = modelReviews.get(position).getComment();

        holder.cusName.setText(cusName);
        holder.ratingBar.setRating(rating);
        holder.comment.setText(comment);
        Picasso.with(mContext).
                load(cusAvt)
                .into(holder.customerAvt);

    }

    @Override
    public int getItemCount() {
        return modelReviews.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView cusName;
        TextView comment;
        ImageView customerAvt;
        RatingBar ratingBar;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.rating_bar);
            comment = itemView.findViewById(R.id.txt_comment);
            cusName = itemView.findViewById(R.id.txt_customer_name);
            customerAvt = itemView.findViewById(R.id.customer_avt);
            cardView = itemView.findViewById(R.id.card_view_review);

        }
    }
}

