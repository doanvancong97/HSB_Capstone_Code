package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.DetailSalonActivity;
import capstone.sonnld.hairsalonbooking.DetailServiceActivity;
import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.ModelSalon;

public class RecyclerViewSalonByRatingAdapter extends RecyclerView.Adapter<RecyclerViewSalonByRatingAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ModelSalon> modelSalons;
    private String des;
    private String salonName;
    private float rating;
    private String imgUrl;
    private String address;

    public RecyclerViewSalonByRatingAdapter(Context mContext, ArrayList<ModelSalon> modelSalons) {
        this.mContext = mContext;
        this.modelSalons = modelSalons;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_salon_by_rating, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //show item

        salonName = modelSalons.get(position).getName();
        address = modelSalons.get(position).getModelAddress().getStreetNumber() + ", "
                + modelSalons.get(position).getModelAddress().getStreet() + ", "
                + modelSalons.get(position).getModelAddress().getModelDistrict().getDistrictName() + ", "
                + modelSalons.get(position).getModelAddress().getModelDistrict().getModelCity().getCityName();
        imgUrl = modelSalons.get(position).getUrl();
        rating = modelSalons.get(position).getAverageRating();

        holder.salonAddress.setText(address);
        holder.salonName.setText(salonName);
        holder.rating.setText(Math.floor(rating*10)/10 + "");
        Picasso.with(mContext).
                load(imgUrl)
                .into(holder.imgSalonThumb);

        //event when tap on a item
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass data to DetailSalonActivity
                Intent intent = new Intent(mContext, DetailSalonActivity.class);
                intent.putExtra("SalonId", modelSalons.get(position).getSalonId());
                intent.putExtra("SalonName", modelSalons.get(position).getName());
                intent.putExtra("SalonStartTime", modelSalons.get(position).getOpenTime());
                intent.putExtra("SalonEndTime", modelSalons.get(position).getCloseTime());
                intent.putExtra("SalonSlotTime", modelSalons.get(position).getSlotTime());
                intent.putExtra("SalonBookingDay", modelSalons.get(position).getBookingDay());
                intent.putExtra("SalonBookingPerSlot", modelSalons.get(position).getBookingPerSlot());
                intent.putExtra("AvgRating", modelSalons.get(position).getAverageRating());



                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelSalons.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView rating;
        TextView salonName;
        TextView salonAddress;
        ImageView imgSalonThumb;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            salonAddress = itemView.findViewById(R.id.salon_address);
            rating = itemView.findViewById(R.id.rating);
            salonName = itemView.findViewById(R.id.salon_name);
            imgSalonThumb = itemView.findViewById(R.id.salon_img);
            cardView = itemView.findViewById(R.id.card_view_salon_by_rating);

        }
    }
}

