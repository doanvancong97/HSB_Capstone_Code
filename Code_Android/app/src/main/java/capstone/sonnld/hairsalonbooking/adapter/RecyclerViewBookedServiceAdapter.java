package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.ModelBookingDetail;

public class RecyclerViewBookedServiceAdapter extends RecyclerView.Adapter<RecyclerViewBookedServiceAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ModelBookingDetail> modelBookingDetails;
    private String serviceName;
    private String discountValue;
    private String price;
    private String salePrice;


    public RecyclerViewBookedServiceAdapter(Context mContext, ArrayList<ModelBookingDetail> modelBookingDetails) {
        this.mContext = mContext;
        this.modelBookingDetails = modelBookingDetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_selected_service, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //show item
        serviceName = modelBookingDetails.get(position).getModelSalonService().getModelService().getServiceName();
        price = modelBookingDetails.get(position).getModelSalonService().getPrice() + "";
        discountValue = modelBookingDetails.get(position).getModelSalonService().getModelDiscount().getDiscountValue() +"";
        salePrice = getSalePrice(price, discountValue);

        holder.txtSalonServiceName.setText(uppercaseFirstLetter(serviceName));
        holder.txtServicePrice.setText(salePrice);

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
        return modelBookingDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtSalonServiceName;
        TextView txtServicePrice;

        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);


            txtSalonServiceName = itemView.findViewById(R.id.service_name);
            txtServicePrice = itemView.findViewById(R.id.service_price);
            cardView = itemView.findViewById(R.id.card_view_selected_service);

        }
    }
}

