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

import capstone.sonnld.hairsalonbooking.DetailServiceActivity;
import capstone.sonnld.hairsalonbooking.HistoryDetailActivity;
import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.Booking;

public class RecyclerViewBookingHistoryAdapter extends RecyclerView.Adapter<RecyclerViewBookingHistoryAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Booking> listBooking;

    private final String CANCEL = "Đơn đặt chỗ đã bị hủy";
    private final String PROCESS = "Đơn đặt chỗ đang được xử lý";
    private final String FINISH = "Đơn đặt chỗ đã hoàn thành";
    private String salonName;
    private String bookedDate;
    private String bookedTime;
    private String status;
    private String salonAddress;
    private String imgUrl;

    public RecyclerViewBookingHistoryAdapter(Context mContext, ArrayList<Booking> listBooking) {
        this.mContext = mContext;
        this.listBooking = listBooking;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_booking_history, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //show item

        salonName = listBooking.get(position).getBookingDetailsCollection().get(0).getSalonService()
                .getSalon().getName();
        salonAddress = listBooking.get(position).getBookingDetailsCollection().get(0).getSalonService()
                .getSalon().getAddress().getStreetNumber() +","+
                listBooking.get(position).getBookingDetailsCollection().get(0).getSalonService()
                        .getSalon().getAddress().getStreet();
        status = listBooking.get(position).getStatus();

        String[] bookedDateArr = listBooking.get(position).getBookingDate().split("-");
        bookedDate = ", ngày " + bookedDateArr[2]+"/"+bookedDateArr[1]+"/"+bookedDateArr[0];

        String[] bookedTimeArr = listBooking.get(position).getBookingTime().split(":");

        bookedTime = "Thời gian: " + bookedTimeArr[0]+":"+bookedTimeArr[1];
        imgUrl = listBooking.get(position).getBookingDetailsCollection().get(0).getSalonService().getSalon().getUrl();

        if(status.equals("process")){
            holder.txtStatus.setTextColor(Color.BLUE);
            holder.txtStatus.setText(PROCESS);
        }else if(status.equals("cancel")){
            holder.txtStatus.setTextColor(Color.RED);
            holder.txtStatus.setText(CANCEL);
        }else if(status.equals("finish")){
            holder.txtStatus.setTextColor(Color.GREEN);
            holder.txtStatus.setText(FINISH);
        }

        holder.txtSalonName.setText(uppercaseFirstLetter(salonName));
        holder.txtBookedTime.setText(bookedTime);
        holder.txtBookedDate.setText(bookedDate);
        holder.txtSalonAddress.setText(salonAddress);
        Picasso.with(mContext)
                .load(imgUrl)
                .into(holder.imgServiceThumb);


//        // event when tap on a item
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass data to HistoryDetailActivity
                Intent intent = new Intent(mContext, HistoryDetailActivity.class);
                intent.putExtra("BookedTime",listBooking.get(position).getBookingTime());
                intent.putExtra("BookedDate", listBooking.get(position).getBookingDate());
                intent.putExtra("Address", listBooking.get(position).getBookingDetailsCollection().get(0).getSalonService()
                        .getSalon().getAddress().getStreetNumber() +
                        listBooking.get(position).getBookingDetailsCollection().get(0).getSalonService()
                                .getSalon().getAddress().getStreet());
                intent.putExtra("SelectedService", listBooking.get(position).getBookingDetailsCollection());
                intent.putExtra("BookingId", listBooking.get(position).getBookingId());

                mContext.startActivity(intent);

            }
        });

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
        return listBooking.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtSalonName;
        TextView txtBookedDate;
        TextView txtBookedTime;
        TextView txtStatus;
        TextView txtSalonAddress;
        ImageView imgServiceThumb;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtSalonName = itemView.findViewById(R.id.txt_salon_name);
            txtStatus = itemView.findViewById(R.id.txt_status);
            txtBookedDate = itemView.findViewById(R.id.txt_booked_date);
            txtBookedTime = itemView.findViewById(R.id.txt_booked_time);
            txtSalonAddress = itemView.findViewById(R.id.salon_address);
            imgServiceThumb = itemView.findViewById(R.id.salon_img);
            cardView = itemView.findViewById(R.id.card_view_booking_history);


        }
    }
}

