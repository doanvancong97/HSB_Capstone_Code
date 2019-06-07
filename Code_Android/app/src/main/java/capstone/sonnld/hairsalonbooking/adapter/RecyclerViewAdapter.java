package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import capstone.sonnld.hairsalonbooking.model.Salon;
import capstone.sonnld.hairsalonbooking.DetailSalonActivity;
import capstone.sonnld.hairsalonbooking.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Salon> mSalonList;

    public RecyclerViewAdapter(Context mContext, List<Salon> mSalonList) {
        this.mContext = mContext;
        this.mSalonList = mSalonList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_salon,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //show item
        holder.txtSalonName.setText(mSalonList.get(position).getSalonName());
//        holder.imgSalonThumb.setImageResource(mSalonList.get(position).getThumbnail());
        holder.txtSalonAddress.setText(mSalonList.get(position).getAddress());
        holder.txtSaleValue.setText(" - " + mSalonList.get(position).getSaleValue());
        Picasso.with(mContext).
                load(mSalonList.get(position).getThumbnailUrl())
                .into(holder.imgSalonThumb);

        // event when tap on a item
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass data to Detail salon activity
                Intent intent = new Intent(mContext, DetailSalonActivity.class);
                intent.putExtra("PromotionName",mSalonList.get(position).getPromotionName());
                intent.putExtra("SalonName",mSalonList.get(position).getSalonName());
                intent.putExtra("Description",mSalonList.get(position).getDescription());
                intent.putExtra("Thumbnail",mSalonList.get(position).getThumbnailUrl());
                intent.putExtra("Address",mSalonList.get(position).getAddress());
//                intent.putExtra("ServiceListName",mSalonList.get(position).getSalonServiceListName());
                // data need to be received in DetailSalonA
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mSalonList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtSalonName;
        TextView txtSalonAddress;
        TextView txtSaleValue;
        ImageView imgSalonThumb;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtSalonName = itemView.findViewById(R.id.salon_name);
            txtSalonAddress = itemView.findViewById(R.id.salon_address);
            txtSaleValue = itemView.findViewById(R.id.txt_sale_value);
            imgSalonThumb = itemView.findViewById(R.id.salon_img);
            cardView = itemView.findViewById(R.id.card_view_salon);
        }
    }
}

