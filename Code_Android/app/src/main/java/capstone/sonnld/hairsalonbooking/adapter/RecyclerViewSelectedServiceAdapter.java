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
import capstone.sonnld.hairsalonbooking.model.SalonService;

public class RecyclerViewSelectedServiceAdapter extends RecyclerView.Adapter<RecyclerViewSelectedServiceAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SalonService> salonServices;
    private String serviceName;
    private String discountValue;
    private String price;
    private String salePrice;


    public RecyclerViewSelectedServiceAdapter(Context mContext, ArrayList<SalonService> salonServices) {
        this.mContext = mContext;
        this.salonServices = salonServices;
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
        serviceName = salonServices.get(position).getService().getServiceName();
        price = salonServices.get(position).getPrice();
        discountValue = salonServices.get(position).getDiscount().getDiscountValue();
        salePrice = getSalePrice(price,discountValue);
        String serviceIcon = salonServices.get(position).getIconUrl();

        holder.txtSalonServiceName.setText(uppercaseFirstLetter(serviceName));
        holder.txtServicePrice.setText(salePrice);





        // event when tap on a item



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

