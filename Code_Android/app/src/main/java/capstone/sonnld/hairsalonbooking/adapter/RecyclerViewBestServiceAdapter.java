package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import capstone.sonnld.hairsalonbooking.DetailServiceActivity;
import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;

public class RecyclerViewBestServiceAdapter extends RecyclerView.Adapter<RecyclerViewBestServiceAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ModelSalonService> modelSalonServices;

    private String des;
    private String serviceName;
    private String discountValue;
    private String salonAddress;
    private String saleValue;
    private String price;
    private String imgUrl;


    public RecyclerViewBestServiceAdapter(Context mContext, ArrayList<ModelSalonService> modelSalonServices) {
        this.mContext = mContext;
        this.modelSalonServices = modelSalonServices;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_best_service, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //show item
        serviceName = modelSalonServices.get(position).getModelService().getServiceName();
        salonAddress = modelSalonServices.get(position).getModelSalon().getModelAddress().getStreetNumber() + ", "
                + modelSalonServices.get(position).getModelSalon().getModelAddress().getStreet() + ", "
                + modelSalonServices.get(position).getModelSalon().getModelAddress().getModelDistrict().getDistrictName() + ", "
                + modelSalonServices.get(position).getModelSalon().getModelAddress().getModelDistrict().getModelCity().getCityName();
        saleValue = " - " + modelSalonServices.get(position).getModelDiscount().getDiscountValue() + "%";
        imgUrl = modelSalonServices.get(position).getThumbUrl();
        price = modelSalonServices.get(position).getPrice() + "";
        discountValue = modelSalonServices.get(position).getModelDiscount().getDiscountValue() + "";
        String salonName = modelSalonServices.get(position).getModelSalon().getName();

        String discountStartDate = modelSalonServices.get(position).getModelDiscount().getValidFrom()+" 00:00:00";
        String discountEndDate = modelSalonServices.get(position).getModelDiscount().getValidUntil()+" 23:59:59";

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date sDate = null;
        try {
            sDate = sdf.parse(discountStartDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ss = now.getTime()-sDate.getTime();

        Date eDate = null;
        try {
            eDate = sdf.parse(discountEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long ss2 = eDate.getTime()-now.getTime();


        if(modelSalonServices.get(position).getModelDiscount().getDiscountValue() == 0 || ss < 0 || ss2 < 0){
            holder.txtSaleValue.setVisibility(View.GONE);
            holder.txtServiceSalePrice.setVisibility(View.GONE);
            holder.txtServicePrice.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.txtServicePrice.setGravity(Gravity.CENTER);
        }else {
            holder.txtServicePrice.setPaintFlags(holder.txtServicePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.txtServicePrice.setText(price + "k");
        holder.txtServiceSalePrice.setText(getSalePrice(price,discountValue));
        holder.txtSalonServiceName.setText(salonName + " - " + uppercaseFirstLetter(serviceName));
        holder.txtSalonAddress.setText(salonAddress);
        holder.txtSaleValue.setText(saleValue);
        Picasso.with(mContext)
                .load(imgUrl)
                .into(holder.imgServiceThumb);


        // event when tap on a item
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass data to DetailServiceActivity
                Intent intent = new Intent(mContext, DetailServiceActivity.class);
                intent.putExtra("SalonID", modelSalonServices.get(position).getModelSalon().getSalonId());
                intent.putExtra("ModelSalonService", modelSalonServices.get(position).getModelService().getServiceName());
                intent.putExtra("SalonServicePrice", modelSalonServices.get(position).getPrice());
                intent.putExtra("DiscountValue", modelSalonServices.get(position).getModelDiscount().getDiscountValue());
                intent.putExtra("SalonName", modelSalonServices.get(position).getModelSalon().getName());
                intent.putExtra("Description", modelSalonServices.get(position).getModelSalon().getDescription());
                intent.putExtra("Thumbnail", modelSalonServices.get(position).getModelSalon().getUrl());
                intent.putExtra("Logo", modelSalonServices.get(position).getModelSalon().getLogoUrl());
                intent.putExtra("ModelAddress", modelSalonServices.get(position).getModelSalon().getModelAddress().getStreetNumber() + ", "
                        + modelSalonServices.get(position).getModelSalon().getModelAddress().getStreet() + ", "
                        + modelSalonServices.get(position).getModelSalon().getModelAddress().getModelDistrict().getDistrictName() + ", "
                        + modelSalonServices.get(position).getModelSalon().getModelAddress().getModelDistrict().getModelCity().getCityName());
                intent.putExtra("SalonStartTime",modelSalonServices.get(position).getModelSalon().getOpenTime());
                intent.putExtra("SalonCloseTime",modelSalonServices.get(position).getModelSalon().getCloseTime());
                intent.putExtra("SalonSlotTime",modelSalonServices.get(position).getModelSalon().getSlotTime());
                intent.putExtra("SalonBookingDay",modelSalonServices.get(position).getModelSalon().getBookingDay());
                intent.putExtra("BookingPerSlot",modelSalonServices.get(position).getModelSalon().getBookingPerSlot());
                intent.putExtra("AvgRating",modelSalonServices.get(position).getModelSalon().getAverageRating());
                intent.putExtra("DiscountEndDate",modelSalonServices.get(position).getModelDiscount().getValidUntil());
                intent.putExtra("DiscountStartDate",modelSalonServices.get(position).getModelDiscount().getValidFrom());
                mContext.startActivity(intent);

            }
        });

    }

    public String getSalePrice(String price,String discountValue){

        int nSalePrice = Integer.parseInt(price);
        int nDiscountValue = Integer.parseInt(discountValue);
        nSalePrice = nSalePrice - (nSalePrice * nDiscountValue / 100);
        return "" + nSalePrice + "K";
    }

    public String uppercaseFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public int getItemCount() {
        return modelSalonServices.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtSalonServiceName;
        TextView txtSalonAddress;
        TextView txtSaleValue;
        TextView txtServicePrice;
        TextView txtServiceSalePrice;
        ImageView imgServiceThumb;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtSalonServiceName = itemView.findViewById(R.id.salon_service_name);
            txtServicePrice = itemView.findViewById(R.id.txt_service_price);
            txtServiceSalePrice = itemView.findViewById(R.id.txt_service_sale_price);
            txtSalonAddress = itemView.findViewById(R.id.salon_address);
            txtSaleValue = itemView.findViewById(R.id.txt_sale_value);
            imgServiceThumb = itemView.findViewById(R.id.salon_img);
            cardView = itemView.findViewById(R.id.card_view_best_service);

        }
    }
}

