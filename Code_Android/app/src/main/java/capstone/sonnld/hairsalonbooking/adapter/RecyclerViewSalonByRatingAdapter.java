package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import capstone.sonnld.hairsalonbooking.DetailSalonActivity;
import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.BookingDetail;

public class RecyclerViewSalonByRatingAdapter extends RecyclerView.Adapter<RecyclerViewSalonByRatingAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<BookingDetail> bookingDetailList;
    private String des;
    private String serviceName;
    private String salonName;
    private String salonAddress;
    private String saleValue;
    private String rate;
    private String imgUrl;
    private String discountValue;
    private String price;

    public RecyclerViewSalonByRatingAdapter(Context mContext, ArrayList<BookingDetail> bookingDetailList) {
        this.mContext = mContext;
        this.bookingDetailList = bookingDetailList;
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_salon_service_by_rating,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, final int position) {
        //show item
         des = "ÁP DỤNG KHI DÙNG DỊCH VỤ TẠI CỬA HÀNG* \n" +
                "\n" +
                "- Giảm 20% tổng hóa đơn áp dụng cho tất cả các dịch vụ \n" +
                "- Áp dụng cho khách hàng nữ \n" +
                "- Mỗi mã ưu đãi đổi được nhiều suất trong suốt chương trình \n" +
                "- Khách hàng có thể lấy nhiều mã trong suốt chương trình \n" +
                "\n" +
                "THỜI GIAN ÁP DỤNG \n" +
                "- Khung giờ: 9h30 - 19h00\t\n" +
                "- Áp dụng tất cả các ngày trong tuần \n" +
                "- Không áp dụng các ngày lễ, Tết: 30/4, 1/5 \n" +
                "\n" +
                "Chi tiết địa điểm xem tại \"Điểm áp dụng\" \n" +
                "\n" +
                "Vui lòng bấm XÁC NHẬN ĐẶT CHỖ để nhận mã giảm giá \n" +
                "\n" +
                "LƯU Ý \n" +
                "- Chương trình chỉ áp dụng với khách dùng dịch vụ tại cửa hàng \n" +
                "- Không áp dụng đồng thời với các chương trình khác của MIA.Nails & Cafe \n" +
                "- Không áp dụng phụ thu \n" +
                "- Ưu đãi chưa bao gồm VAT \n" +
                "- Khách hàng được phép đến sớm hoặc muộn hơn 15 phút so với giờ hẹn đến \n" +
                "- Mã giảm giá không có giá trị quy đổi thành tiền mặt ";

         serviceName = bookingDetailList.get(position).getSalonService().getService().getServiceName();
         salonName = bookingDetailList.get(position).getSalonService().getSalon().getName();
         salonAddress = bookingDetailList.get(position).getSalonService().getSalon().getAddress().getStreetNumber()+", "
                 +bookingDetailList.get(position).getSalonService().getSalon().getAddress().getStreet();
         saleValue = " - " + bookingDetailList.get(position).getSalonService().getDiscount().getDiscountValue()+"%";
         rate = bookingDetailList.get(position).getReview().getRating();
         imgUrl = bookingDetailList.get(position).getSalonService().getSalon().getUrl();
         price = bookingDetailList.get(position).getSalonService().getPrice();
         discountValue = bookingDetailList.get(position).getSalonService().getDiscount().getDiscountValue();

        holder.txtServiceName.setText(uppercaseFirstLetter(serviceName));
        holder.txtPrice.setText(price);
        holder.txtServiceSalePrice.setText(getSalePrice(price,discountValue));
        holder.txtSalonAddress.setText(salonAddress);
        holder.txtSaleValue.setText(saleValue);
        holder.txtRate.setText(rate);
        Picasso.with(mContext).
                load(imgUrl)
                .into(holder.imgSalonThumb);

         //event when tap on a item
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass data to Detail salon activity
                Intent intent = new Intent(mContext, DetailSalonActivity.class);
                intent.putExtra("SalonID",bookingDetailList.get(position).getSalonService().getSalon().getSalonId());
                intent.putExtra("SalonService", bookingDetailList.get(position).getSalonService().getService().getServiceName());
                intent.putExtra("SalonServicePrice", bookingDetailList.get(position).getSalonService().getPrice());
                intent.putExtra("DiscountValue", bookingDetailList.get(position).getSalonService().getDiscount().getDiscountValue());
                intent.putExtra("SalonName", bookingDetailList.get(position).getSalonService().getSalon().getName());
                intent.putExtra("Description", des);
                intent.putExtra("Thumbnail", bookingDetailList.get(position).getSalonService().getSalon().getUrl());
                intent.putExtra("Address", bookingDetailList.get(position).getSalonService().getSalon().getAddress().getStreet());
                // data need to be received in DetailSalonA
                mContext.startActivity(intent);

            }
        });
    }

    public String getSalePrice(String price,String discountValue){

        String sSalePrice = price.substring(0, price.length() - 1);
        int nSalePrice = Integer.parseInt(sSalePrice);
        int nDiscountValue = Integer.parseInt(discountValue);
        nSalePrice = nSalePrice - (nSalePrice * nDiscountValue / 100);

        return nSalePrice + "k";
    }

    public String uppercaseFirstLetter(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public int getItemCount() {
        return bookingDetailList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtSalonName;
        TextView txtPrice;
        TextView txtServiceSalePrice;
        TextView txtServiceName;
        TextView txtSalonAddress;
        TextView txtSaleValue;
        TextView txtRate;
        ImageView imgSalonThumb;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtServiceName = itemView.findViewById(R.id.txt_service_name);
            txtPrice = itemView.findViewById(R.id.txt_service_price);
            txtServiceSalePrice = itemView.findViewById(R.id.txt_service_sale_price);
            txtSalonAddress = itemView.findViewById(R.id.txt_salon_address);
            txtSaleValue = itemView.findViewById(R.id.txt_sale_value);
            txtRate = itemView.findViewById(R.id.salon_rate);
            imgSalonThumb = itemView.findViewById(R.id.salon_img);
            cardView = itemView.findViewById(R.id.card_view_item_salon_service_by_rating);

            txtPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}

