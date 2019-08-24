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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import capstone.sonnld.hairsalonbooking.DetailServiceActivity;
import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;

public class RecyclerViewFilterServiceAdapter extends RecyclerView.Adapter<RecyclerViewFilterServiceAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<ModelSalonService> modelSalonServices;
    private ArrayList<ModelSalonService> modelSalonServicesFilterList;
    private String des;
    private String serviceName;
    private String discountValue;
    private String salonAddress;
    private String saleValue;
    private String price;
    private String imgUrl;


    public RecyclerViewFilterServiceAdapter(Context mContext, ArrayList<ModelSalonService> modelSalonServices) {
        this.mContext = mContext;
        this.modelSalonServices = modelSalonServices;
        modelSalonServicesFilterList = new ArrayList<>(modelSalonServices);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_filter_salon_service, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //show item
        des = modelSalonServices.get(position).getModelSalon().getDescription();
        serviceName = modelSalonServices.get(position).getModelService().getServiceName();
        salonAddress = modelSalonServices.get(position).getModelSalon().getModelAddress().getStreetNumber() + ", "
                + modelSalonServices.get(position).getModelSalon().getModelAddress().getStreet() + ", "
                + modelSalonServices.get(position).getModelSalon().getModelAddress().getModelDistrict().getDistrictName() + ", "
                + modelSalonServices.get(position).getModelSalon().getModelAddress().getModelDistrict().getModelCity().getCityName();
        saleValue = " - " + modelSalonServices.get(position).getModelDiscount().getDiscountValue() + "%";
        imgUrl = modelSalonServices.get(position).getThumbUrl();
        price = modelSalonServices.get(position).getPrice() + "";
        discountValue = modelSalonServices.get(position).getModelDiscount().getDiscountValue() + "";

        holder.txtServicePrice.setText(price + "k");
        holder.txtServiceSalePrice.setText(getSalePrice(price, discountValue));
        holder.txtSalonServiceName.setText(uppercaseFirstLetter(serviceName));
        holder.txtSalonAddress.setText(salonAddress);
        holder.txtSaleValue.setText(saleValue);
        Picasso.with(mContext).
                load(imgUrl)
                .into(holder.imgSalonThumb);


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

                intent.putExtra("SalonStartTime", modelSalonServices.get(position).getModelSalon().getOpenTime());
                intent.putExtra("SalonCloseTime", modelSalonServices.get(position).getModelSalon().getCloseTime());
                intent.putExtra("SalonSlotTime", modelSalonServices.get(position).getModelSalon().getSlotTime());
                intent.putExtra("SalonBookingDay", modelSalonServices.get(position).getModelSalon().getBookingDay());
                intent.putExtra("BookingPerSlot", modelSalonServices.get(position).getModelSalon().getBookingPerSlot());
                intent.putExtra("AvgRating", modelSalonServices.get(position).getModelSalon().getAverageRating());
                intent.putExtra("DiscountEndDate", modelSalonServices.get(position).getModelDiscount().getValidUntil());

                mContext.startActivity(intent);

            }
        });

        holder.txtSalonServiceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, modelSalonServices.get(position).getModelService().getServiceName(), Toast.LENGTH_LONG).show();
            }
        });


    }

    public String getSalePrice(String price, String discountValue) {

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

    @Override
    public Filter getFilter() {
        return serviceFilter;
    }

    private Filter serviceFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ModelSalonService> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(modelSalonServicesFilterList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ModelSalonService item : modelSalonServicesFilterList) {
                    if (removeAccent(item.getModelService().getServiceName()).toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modelSalonServices.clear();
            modelSalonServices.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp)
                .replaceAll("")
                .replaceAll("Đ", "D").replace("đ", "d");
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtSalonServiceName;
        TextView txtSalonAddress;
        TextView txtSaleValue;
        TextView txtServicePrice;
        TextView txtServiceSalePrice;
        ImageView imgSalonThumb;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtSalonServiceName = itemView.findViewById(R.id.salon_service_name);
            txtServicePrice = itemView.findViewById(R.id.txt_service_price);
            txtServiceSalePrice = itemView.findViewById(R.id.txt_service_sale_price);
            txtSalonAddress = itemView.findViewById(R.id.salon_address);
            txtSaleValue = itemView.findViewById(R.id.txt_sale_value);
            imgSalonThumb = itemView.findViewById(R.id.salon_img);
            cardView = itemView.findViewById(R.id.card_view_filter_salon_service);

            txtServicePrice.setPaintFlags(txtServicePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}

