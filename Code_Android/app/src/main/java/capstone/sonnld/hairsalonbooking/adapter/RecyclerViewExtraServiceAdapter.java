package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.SalonService;

public class RecyclerViewExtraServiceAdapter extends RecyclerView.Adapter<RecyclerViewExtraServiceAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SalonService> salonServices;
    private ArrayList<SalonService> checkedSalonServices = new ArrayList<>();
    private String serviceName;

    private String discountValue;
    private String serviceSalePrice;


    private String price;

    public RecyclerViewExtraServiceAdapter(Context mContext, ArrayList<SalonService> salonServices) {
        this.mContext = mContext;
        this.salonServices = salonServices;
    }

    public ArrayList<SalonService> getCheckedSalonServices() {
        return checkedSalonServices;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_service,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        //show item

        serviceName = salonServices.get(position).getService().getServiceName();
        discountValue = salonServices.get(position).getDiscount().getDiscountValue();
        price = salonServices.get(position).getPrice();
        serviceSalePrice = getSalePrice(price,discountValue);

        holder.txtSalonServiceName.setText(uppercaseFirstLetter(serviceName) + " ( -" + discountValue + "% )" );
        holder.txtPrice.setText(price);
        holder.txtSalePrice.setText(serviceSalePrice);
        holder.imgServiceIcon.setImageResource(R.drawable.kid);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                CheckBox chk = (CheckBox) view;
                if(chk.isChecked()){
                    checkedSalonServices.add(salonServices.get(pos));
                }else if(!chk.isChecked()){
                    checkedSalonServices.remove(salonServices.get(pos));
                }
            }
        });
//        Picasso.with(mContext).
//                load(imgUrl)
//                .into(holder.imgServiceIcon);

         //event when click on checkbox
//
    }
    public String getSalePrice(String price,String discountValue){

        String sSalePrice = price.substring(0, price.length() - 1);
        int nSalePrice = Integer.parseInt(sSalePrice);
        int nDiscountValue = Integer.parseInt(discountValue);
        nSalePrice = nSalePrice - (nSalePrice * nDiscountValue / 100);

        return "" + nSalePrice + "k";
    }

    public String uppercaseFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    @Override
    public int getItemCount() {
        return salonServices.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView txtSalonServiceName;
        TextView txtPrice;
        TextView txtSalePrice;
        CheckBox chkBox;
        ImageView imgServiceIcon;
        CardView cardView;

        ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtSalonServiceName = itemView.findViewById(R.id.service_name);
            txtPrice = itemView.findViewById(R.id.txt_salon_service_price);
            txtSalePrice = itemView.findViewById(R.id.txt_service_sale_price);
            chkBox = itemView.findViewById(R.id.chkBox);
            imgServiceIcon = itemView.findViewById(R.id.service_icon);
            cardView = itemView.findViewById(R.id.card_view_service);

            txtPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            chkBox.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener = ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
    }
}

