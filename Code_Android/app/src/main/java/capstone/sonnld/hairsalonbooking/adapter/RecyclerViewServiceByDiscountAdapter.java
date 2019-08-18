package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.DetailServiceActivity;
import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;

public class RecyclerViewServiceByDiscountAdapter extends RecyclerView.Adapter<RecyclerViewServiceByDiscountAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ModelSalonService> modelSalonServices;

    private String des;
    private String serviceName;
    private String discountValue;
    private String salonAddress;
    private String saleValue;
    private String price;
    private String imgUrl;


    public RecyclerViewServiceByDiscountAdapter(Context mContext, ArrayList<ModelSalonService> modelSalonServices) {
        this.mContext = mContext;
        this.modelSalonServices = modelSalonServices;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_salon_service_by_discount, parent, false);

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
        price = modelSalonServices.get(position).getPrice();
        discountValue = modelSalonServices.get(position).getModelDiscount().getDiscountValue();
        String salonName = modelSalonServices.get(position).getModelSalon().getName();


        holder.txtSalonName.setText(salonName);
        holder.txtServicePrice.setText(price);
        holder.txtServiceSalePrice.setText(getSalePrice(price,discountValue));
        holder.txtSalonServiceName.setText(uppercaseFirstLetter(serviceName));
        holder.txtSalonAddress.setText(salonAddress);
        holder.txtSaleValue.setText(saleValue);
        Picasso.with(mContext)
                .load(imgUrl)
                .into(holder.imgServiceThumb);


        // event when tap on a item
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass data to Detail salon activity
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
                mContext.startActivity(intent);

            }
        });

    }

    public String getSalePrice(String price,String discountValue){

        String sSalePrice = price.substring(0, price.length() - 1);
        int nSalePrice = Integer.parseInt(sSalePrice);
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
        TextView txtSalonName;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtSalonServiceName = itemView.findViewById(R.id.salon_service_name);
            txtSalonName = itemView.findViewById(R.id.salon_name);
            txtServicePrice = itemView.findViewById(R.id.txt_service_price);
            txtServiceSalePrice = itemView.findViewById(R.id.txt_service_sale_price);
            txtSalonAddress = itemView.findViewById(R.id.salon_address);
            txtSaleValue = itemView.findViewById(R.id.txt_sale_value);
            imgServiceThumb = itemView.findViewById(R.id.salon_img);
            cardView = itemView.findViewById(R.id.card_view_salon_service);
            txtServicePrice.setPaintFlags(txtServicePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}

