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
import capstone.sonnld.hairsalonbooking.model.SalonService;

public class RecyclerViewFilterServiceAdapter extends RecyclerView.Adapter<RecyclerViewFilterServiceAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<SalonService> salonServices;
    private ArrayList<SalonService> salonServicesFilterList;
    private String des;
    private String serviceName;
    private String discountValue;
    private String salonAddress;
    private String saleValue;
    private String price;
    private String imgUrl;


    public RecyclerViewFilterServiceAdapter(Context mContext, ArrayList<SalonService> salonServices) {
        this.mContext = mContext;
        this.salonServices = salonServices;
        salonServicesFilterList = new ArrayList<>(salonServices);
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
        des = salonServices.get(position).getSalon().getDescription();
        serviceName = salonServices.get(position).getService().getServiceName();
        salonAddress = salonServices.get(position).getSalon().getAddress().getStreetNumber() + ", "
                + salonServices.get(position).getSalon().getAddress().getStreet();
        saleValue = " - " + salonServices.get(position).getDiscount().getDiscountValue() + "%";
        imgUrl = salonServices.get(position).getThumbUrl();
        price = salonServices.get(position).getPrice();
        discountValue = salonServices.get(position).getDiscount().getDiscountValue();

        holder.txtServicePrice.setText(price);
        holder.txtServiceSalePrice.setText(getSalePrice(price,discountValue));
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
                intent.putExtra("SalonID",salonServices.get(position).getSalon().getSalonId());
                intent.putExtra("SalonService", salonServices.get(position).getService().getServiceName());
                intent.putExtra("SalonServicePrice", salonServices.get(position).getPrice());
                intent.putExtra("DiscountValue", salonServices.get(position).getDiscount().getDiscountValue());
                intent.putExtra("SalonName", salonServices.get(position).getSalon().getName());
                intent.putExtra("Description", des);
                intent.putExtra("Thumbnail", salonServices.get(position).getSalon().getUrl());
                intent.putExtra("Address", salonServices.get(position).getSalon().getAddress().getStreet());
                // data need to be received in DetailSalonA
                mContext.startActivity(intent);

            }
        });

        holder.txtSalonServiceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,salonServices.get(position).getService().getServiceName(),Toast.LENGTH_LONG).show();
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
        return salonServices.size();
    }

    @Override
    public Filter getFilter() {
        return serviceFilter;
    }

    private Filter serviceFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SalonService> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(salonServicesFilterList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (SalonService item : salonServicesFilterList) {
                    if (removeAccent(item.getService().getServiceName()).toLowerCase().contains(filterPattern)) {
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
            salonServices.clear();
            salonServices.addAll((ArrayList) results.values);
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

