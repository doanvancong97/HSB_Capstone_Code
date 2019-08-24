package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;

public class RecyclerViewExtraServiceAdapter extends RecyclerView.Adapter<RecyclerViewExtraServiceAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ModelSalonService> modelSalonServices;
    private ArrayList<ModelSalonService> checkedModelSalonServices = new ArrayList<>();
    private String serviceName;

    private String discountValue;
    private String serviceSalePrice;
    private String price;
    private int excuteTime;

    public RecyclerViewExtraServiceAdapter(Context mContext, ArrayList<ModelSalonService> modelSalonServices) {
        this.mContext = mContext;
        this.modelSalonServices = modelSalonServices;
    }

    public ArrayList<ModelSalonService> getCheckedModelSalonServices() {
        return checkedModelSalonServices;
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

        serviceName = modelSalonServices.get(position).getModelService().getServiceName();
        discountValue = modelSalonServices.get(position).getModelDiscount().getDiscountValue() + "";
        price = modelSalonServices.get(position).getPrice() +"";
        serviceSalePrice = getSalePrice(price,discountValue);
        excuteTime = modelSalonServices.get(position).getExecuteTime();

        holder.txtSalonServiceName.setText(uppercaseFirstLetter(serviceName) + " (-" + discountValue + "%)" );
        holder.txtPrice.setText(price + "k");
        holder.txtSalePrice.setText(serviceSalePrice);
        holder.txtExecuteTime.setText(excuteTime + " phút");

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                CheckBox chk = (CheckBox) view;
                if(chk.isChecked()){
                    checkedModelSalonServices.add(modelSalonServices.get(pos));
                }else if(!chk.isChecked()){
                    checkedModelSalonServices.remove(modelSalonServices.get(pos));
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView txtSalonServiceName;
        TextView txtPrice;
        TextView txtSalePrice;
        TextView txtExecuteTime;
        CheckBox chkBox;

        CardView cardView;

        ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtSalonServiceName = itemView.findViewById(R.id.service_name);
            txtPrice = itemView.findViewById(R.id.txt_salon_service_price);
            txtSalePrice = itemView.findViewById(R.id.txt_service_sale_price);
            txtExecuteTime = itemView.findViewById(R.id.txt_salon_service_time);
            chkBox = itemView.findViewById(R.id.chkBox);

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

