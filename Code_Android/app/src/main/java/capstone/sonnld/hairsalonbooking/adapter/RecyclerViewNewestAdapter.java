package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.Salon;
import capstone.sonnld.hairsalonbooking.model.SalonService;

public class RecyclerViewNewestAdapter extends RecyclerView.Adapter<RecyclerViewNewestAdapter.MyViewHolder> {

    private Context mContext;
    private List<SalonService> salonServices;

    public RecyclerViewNewestAdapter(Context mContext, List<SalonService> salonServices) {
        this.mContext = mContext;
        this.salonServices = salonServices;
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_newest,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, final int position) {
        //show item
        holder.txtSalonName.setText(salonServices.get(position).getService().getServiceName());
//        holder.imgSalonThumb.setImageResource(salonServices.get(position).getThumbnail());
        holder.txtSalonAddress.setText(salonServices.get(position).getSalon().getAddress());
        holder.txtSaleValue.setText(" - " + salonServices.get(position).getDiscount().getDiscountValue());
        holder.txtRate.setText( "5" );
        Picasso.with(mContext).
                load(salonServices.get(position).getSalon().getUrl())
                .into(holder.imgSalonThumb);

        // event when tap on a item
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //pass data to Detail salon activity
//                Intent intent = new Intent(mContext, DetailSalonActivity.class);
//                intent.putExtra("SalonName",salonServices.get(position).getSalonName());
//                intent.putExtra("Description",salonServices.get(position).getDescription());
//                intent.putExtra("Thumbnail",salonServices.get(position).getThumbnailUrl());
//                intent.putExtra("Address",salonServices.get(position).getAddress());
////                intent.putExtra("ServiceListName",salonServices.get(position).getSalonServiceListName());
//                // data need to be received in DetailSalonA
//                mContext.startActivity(intent);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return salonServices.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtSalonName;
        TextView txtSalonAddress;
        TextView txtSaleValue;
        TextView txtRate;
        ImageView imgSalonThumb;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtSalonName = itemView.findViewById(R.id.salon_name);
            txtSalonAddress = itemView.findViewById(R.id.salon_address);
            txtSaleValue = itemView.findViewById(R.id.txt_sale_value);
            txtRate = itemView.findViewById(R.id.salon_rate);
            imgSalonThumb = itemView.findViewById(R.id.salon_img);
            cardView = itemView.findViewById(R.id.card_view_item_newest);
        }
    }
}

