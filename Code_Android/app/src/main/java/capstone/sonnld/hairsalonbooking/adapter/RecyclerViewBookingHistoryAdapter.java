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
import capstone.sonnld.hairsalonbooking.model.ModelBooking;

public class RecyclerViewBookingHistoryAdapter extends RecyclerView.Adapter<RecyclerViewBookingHistoryAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ModelBooking> listModelBooking;

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

    public RecyclerViewBookingHistoryAdapter(Context mContext, ArrayList<ModelBooking> listModelBooking) {
        this.mContext = mContext;
        this.listModelBooking = listModelBooking;
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

        salonName = listModelBooking.get(position).getModelBookingDetailsCollection().get(0).getModelSalonService()
                .getModelSalon().getName();
        salonAddress = listModelBooking.get(position).getModelBookingDetailsCollection().get(0).getModelSalonService()
                .getModelSalon().getModelAddress().getStreetNumber() +", "+
                listModelBooking.get(position).getModelBookingDetailsCollection().get(0).getModelSalonService()
                        .getModelSalon().getModelAddress().getStreet() + ", " +
                listModelBooking.get(position).getModelBookingDetailsCollection().get(0).getModelSalonService()
                        .getModelSalon().getModelAddress().getModelDistrict().getDistrictName() + ", " +
                listModelBooking.get(position).getModelBookingDetailsCollection().get(0).getModelSalonService()
                        .getModelSalon().getModelAddress().getModelDistrict().getModelCity().getCityName();
        status = listModelBooking.get(position).getStatus();

        String[] bookedDateArr = listModelBooking.get(position).getBookingDate().split("-");
        bookedDate = ", ngày " + bookedDateArr[2]+"/"+bookedDateArr[1]+"/"+bookedDateArr[0];

        String[] bookedTimeArr = listModelBooking.get(position).getBookingTime().split(":");

        bookedTime = "Thời gian: " + bookedTimeArr[0]+":"+bookedTimeArr[1];
        imgUrl = listModelBooking.get(position).getModelBookingDetailsCollection().get(0).getModelSalonService().getModelSalon().getUrl();

        if(status.equals("1process")){
            holder.txtStatus.setTextColor(Color.BLUE);
            holder.txtStatus.setText(PROCESS);
        }else if(status.equals("2cancel")) {
            holder.txtStatus.setTextColor(Color.RED);
            holder.txtStatus.setText(CANCEL);
        }else if(status.equals("3salonCancel")){
            holder.txtStatus.setTextColor(Color.RED);
            holder.txtStatus.setText(SALON_CANCEL);
        }else if(status.equals("4finish")){
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
                intent.putExtra("BookedTime", listModelBooking.get(position).getBookingTime());
                intent.putExtra("BookedDate", listModelBooking.get(position).getBookingDate());
                intent.putExtra("ModelAddress", listModelBooking.get(position).getModelBookingDetailsCollection().get(0).getModelSalonService()
                        .getModelSalon().getModelAddress().getStreetNumber() +", "+
                        listModelBooking.get(position).getModelBookingDetailsCollection().get(0).getModelSalonService()
                                .getModelSalon().getModelAddress().getStreet() + ", " +
                        listModelBooking.get(position).getModelBookingDetailsCollection().get(0).getModelSalonService()
                                .getModelSalon().getModelAddress().getModelDistrict().getDistrictName() + ", " +
                        listModelBooking.get(position).getModelBookingDetailsCollection().get(0).getModelSalonService()
                                .getModelSalon().getModelAddress().getModelDistrict().getModelCity().getCityName());
                intent.putExtra("SelectedService", listModelBooking.get(position).getModelBookingDetailsCollection());
                intent.putExtra("BookingId", listModelBooking.get(position).getBookingId());
                intent.putExtra("BookingStatus", listModelBooking.get(position).getStatus());
                intent.putExtra("SalonName",listModelBooking.get(position).getModelBookingDetailsCollection().get(0)
                        .getModelSalonService().getModelSalon().getName());
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
        return listModelBooking.size();
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

