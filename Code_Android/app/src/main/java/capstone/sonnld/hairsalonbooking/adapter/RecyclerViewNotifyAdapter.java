package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import capstone.sonnld.hairsalonbooking.HistoryDetailActivity;
import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.ModelNotify;

public class RecyclerViewNotifyAdapter extends RecyclerView.Adapter<RecyclerViewNotifyAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ModelNotify> notifyArrayList;

    private final String CANCEL = "Bạn đã hủy ";
    private final String SALON_CANCEL = "Hủy bởi salon";
    private final String PROCESS = "Đang xử lý";
    private final String FINISH = "Hoàn thành";
    private String salonName;
    private String bookedDate;
    private String bookedTime;
    private String status;
    private String salonAddress;
    private String imgUrl;

    public RecyclerViewNotifyAdapter(Context mContext, ArrayList<ModelNotify> notifyArrayList) {
        this.mContext = mContext;
        this.notifyArrayList = notifyArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_notify, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //show item

        salonName = notifyArrayList.get(position).getModelSalon().getName();

        String statusCode = notifyArrayList.get(position).getStatus();
        if(statusCode.equals("3salonCancel")){
            status = "bị hủy";
        }
        if(statusCode.equals("4finish")){
            status = "hoàn thành";
        }

        String[] bookedDateArr = notifyArrayList.get(position).getBookDate().split("-");
        bookedDate =  bookedDateArr[2]+"/"+bookedDateArr[1]+"/"+bookedDateArr[0];

        String[] bookedTimeArr = notifyArrayList.get(position).getBookTime().split(":");

        bookedTime = bookedTimeArr[0]+":"+bookedTimeArr[1];
        imgUrl = notifyArrayList.get(position).getModelSalon().getUrl();
        String isRead = notifyArrayList.get(position).getIsRead();

        String[] createdDateTimeArr = notifyArrayList.get(position).getCreatedDate().split(" ");
        String[] createdDateArr = createdDateTimeArr[0].split("-");
        String createdDate = createdDateArr[2] + "/" + createdDateArr[1] + "/" + createdDateArr[0];

        if(isRead.equals("yes")){
            holder.txtStatus.setText("Đã xem");
            holder.txtStatus.setTextColor(Color.GREEN);
        }else{
            holder.txtStatus.setText("Chưa xem");
            holder.txtStatus.setTextColor(Color.RED);
        }



        holder.txtTitle.setText("Lịch đặt chỗ đã " + status);
        holder.txtDetail.setText("Lịch đặt chỗ ngày " + bookedDate + " lúc " + bookedTime + " tại salon "
                + salonName + " đã " + status );
        holder.txtCreatedDate.setText(createdDate);
        Picasso.with(mContext)
                .load(imgUrl)
                .into(holder.imgServiceThumb);


//        // event when tap on a item
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass data to HistoryDetailActivity
                Intent intent = new Intent(mContext, HistoryDetailActivity.class);
                intent.putExtra("BookedTime", notifyArrayList.get(position).getBookTime());
                intent.putExtra("BookedDate", notifyArrayList.get(position).getBookDate());
                intent.putExtra("ModelAddress", notifyArrayList.get(position)
                        .getModelSalon().getModelAddress().getStreetNumber() +", "+
                        notifyArrayList.get(position)
                                .getModelSalon().getModelAddress().getStreet() + ", " +
                        notifyArrayList.get(position)
                                .getModelSalon().getModelAddress().getModelDistrict().getDistrictName() + ", " +
                        notifyArrayList.get(position)
                                .getModelSalon().getModelAddress().getModelDistrict().getModelCity().getCityName());
                intent.putExtra("BookingId", notifyArrayList.get(position).getBookId());
                intent.putExtra("BookingStatus", notifyArrayList.get(position).getStatus());
                intent.putExtra("SalonName", notifyArrayList.get(position).getModelSalon().getName());
                mContext.startActivity(intent);

            }
        });

    }


    public String uppercaseFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public int getItemCount() {
        return notifyArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtDetail;
        TextView txtStatus;
        TextView txtCreatedDate;
        ImageView imgServiceThumb;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_title);
            txtStatus = itemView.findViewById(R.id.txt_status);
            txtCreatedDate = itemView.findViewById(R.id.txt_created_date);
            txtDetail = itemView.findViewById(R.id.txt_detail);
            imgServiceThumb = itemView.findViewById(R.id.salon_img);
            cardView = itemView.findViewById(R.id.card_view_notify);


        }
    }
}

