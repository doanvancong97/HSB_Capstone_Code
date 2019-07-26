package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;
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

import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.BookingDetail;

public class RecyclerViewBookedServiceAdapter extends RecyclerView.Adapter<RecyclerViewBookedServiceAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<BookingDetail> bookingDetails;
    private String serviceName;
    private String discountValue;
    private String price;
    private String salePrice;


    public RecyclerViewBookedServiceAdapter(Context mContext, ArrayList<BookingDetail> bookingDetails) {
        this.mContext = mContext;
        this.bookingDetails = bookingDetails;
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
        serviceName = bookingDetails.get(position).getSalonService().getService().getServiceName();
        price = bookingDetails.get(position).getSalonService().getPrice();
        discountValue = bookingDetails.get(position).getSalonService().getDiscount().getDiscountValue();
        salePrice = getSalePrice(price, discountValue);
        String serviceIcon = bookingDetails.get(position).getSalonService().getIconUrl();

        holder.txtSalonServiceName.setText(uppercaseFirstLetter(serviceName));
        holder.txtServicePrice.setText(salePrice);

        Picasso.with(mContext).
                load(serviceIcon)
                .into(holder.imgIcon);

    }

    public String getSalePrice(String price, String discountValue) {

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
        return bookingDetails.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtSalonServiceName;
        TextView txtServicePrice;
        ImageView imgIcon;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgIcon = itemView.findViewById(R.id.service_icon);
            txtSalonServiceName = itemView.findViewById(R.id.service_name);
            txtServicePrice = itemView.findViewById(R.id.service_price);
            cardView = itemView.findViewById(R.id.card_view_selected_service);

        }
    }
}

