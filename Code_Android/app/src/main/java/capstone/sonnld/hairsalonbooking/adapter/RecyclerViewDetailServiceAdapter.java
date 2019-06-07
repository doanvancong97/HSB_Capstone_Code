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

import java.util.List;

import capstone.sonnld.hairsalonbooking.model.SalonService;
import capstone.sonnld.hairsalonbooking.R;

public class RecyclerViewDetailServiceAdapter extends RecyclerView.Adapter<RecyclerViewDetailServiceAdapter.MyViewHolder> {

    private Context mContext;
    private List<SalonService> mSalonServices;

    public RecyclerViewDetailServiceAdapter(Context mContext, List<SalonService> mSalonServices) {
        this.mContext = mContext;
        this.mSalonServices = mSalonServices;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_detail_service,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //show item
        holder.txtServiceName.setText(mSalonServices.get(position).getServiceName());
        holder.imgIcon.setImageResource(mSalonServices.get(position).getServiceIcon());
        holder.txtServicePrice.setText(mSalonServices.get(position).getPrice() + "K");

        // event when tap on a item
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //pass data to Detail salon activity
//                Intent intent = new Intent(mContext, DetailSalonActivity.class);
//                intent.putExtra("SalonName",mSalonList.get(position).getSalonName());
//                intent.putExtra("Description",mSalonList.get(position).getDescription());
//                intent.putExtra("Thumbnail",mSalonList.get(position).getThumbnail());
//                intent.putExtra("Address",mSalonList.get(position).getAddress());
//                // data need to be received in DetailSalonA
//                mContext.startActivity(intent);
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mSalonServices.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtServiceName;
        TextView txtServicePrice;
        ImageView imgIcon;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtServiceName = itemView.findViewById(R.id.service_name);
            txtServicePrice = itemView.findViewById(R.id.service_price);
            imgIcon = itemView.findViewById(R.id.service_img);
            cardView = itemView.findViewById(R.id.card_view_detail_service);
        }
    }
}

