package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import capstone.sonnld.hairsalonbooking.DetailSalonActivity;
import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.model.SalonService;

public class RecyclerViewSalonByRatingAdapter extends RecyclerView.Adapter<RecyclerViewSalonByRatingAdapter.MyViewHolder> {

    private Context mContext;
    private List<SalonService> salonServices;
    private String des;
    private String salonServiceName;
    private String salonAddress;
    private String saleValue;
    private String salonImgUrl;
    private String salonName;
    private String rating;

    public RecyclerViewSalonByRatingAdapter(Context mContext, List<SalonService> salonServices) {
        this.mContext = mContext;
        this.salonServices = salonServices;
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_salon_service_rating,parent,false);

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

        salonServiceName = salonServices.get(position).getService().getServiceName();
        salonAddress = salonServices.get(position).getSalon().getLocation().getStreetAddress() + ", " +
                salonServices.get(position).getSalon().getLocation().getDistrict() + ", " +
                salonServices.get(position).getSalon().getLocation().getCity();
        saleValue = " - " + salonServices.get(position).getDiscount().getDiscountValue();
        salonName = salonServices.get(position).getSalon().getName();
        salonImgUrl = salonServices.get(position).getSalon().getUrl();
        rating = salonServices.get(position).getReview().getRating();

        holder.txtServiceName.setText(capFirstLetter(salonServiceName));
        holder.txtSalonAddress.setText(capFirstLetter(salonAddress));
        holder.txtSaleValue.setText(saleValue);
        holder.txtSalonName.setText(salonName);
        holder.txtRate.setText(rating);
        Picasso.with(mContext).
                load(salonImgUrl)
                .into(holder.imgSalonThumb);

         //event when tap on a item
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass data to Detail salon activity
                Intent intent = new Intent(mContext, DetailSalonActivity.class);
                intent.putExtra("SalonService", salonServices.get(position).getService().getServiceName());
                intent.putExtra("SalonName", salonServices.get(position).getSalon().getName());
                intent.putExtra("Description", des);
                intent.putExtra("Thumbnail", salonServices.get(position).getSalon().getUrl());
                intent.putExtra("Address", salonServices.get(position).getSalon().getLocation().getCity());
//                intent.putExtra("ServiceListName",salonServices.get(position).getSalonServiceListName());
                // data need to be received in DetailSalonA
                mContext.startActivity(intent);

            }
        });
    }

    public String capFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);

    }
    @Override
    public int getItemCount() {
        return salonServices.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtSalonName;
        TextView txtServiceName;
        TextView txtSalonAddress;
        TextView txtSaleValue;
        TextView txtRate;
        ImageView imgSalonThumb;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtServiceName = itemView.findViewById(R.id.txt_service_name);
            txtSalonName = itemView.findViewById(R.id.txt_salon_name);
            txtSalonAddress = itemView.findViewById(R.id.txt_salon_address);
            txtSaleValue = itemView.findViewById(R.id.txt_sale_value);
            txtRate = itemView.findViewById(R.id.salon_rate);
            imgSalonThumb = itemView.findViewById(R.id.salon_img);
            cardView = itemView.findViewById(R.id.card_view_item_newest);
        }
    }
}

