package capstone.sonnld.hairsalonbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<SalonService> salonServices;

    public RecyclerViewAdapter(Context mContext, List<SalonService> salonServices) {
        this.mContext = mContext;
        this.salonServices = salonServices;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_view_item_salon_service,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //show item
        final String txtDumb = "*KHÔNG ÁP DỤNG DỊCH VỤ GIAO HÀNG* \n" +
                "\n" +
                "- Đồng giá 45K/cốc, áp dụng với sản phẩm Sữa tươi kem trứng trân châu đường đen size M (Giá gốc 65K) \n" +
                "\n" +
                "- Ưu đãi không bao gồm topping gọi thêm \n" +
                "\n" +
                "- Mỗi mã ưu đãi chỉ sử dụng 01 lần trong suốt chương trình \n" +
                "- Mỗi mã ưu đãi áp dụng để mua 01 cốc \n" +
                "- Khách hàng có thể lấy nhiều mã trong suốt chương trình \n" +
                "\n" +
                "THỜI GIAN ÁP DỤNG \n" +
                "- Khung giờ: 9h00 - 22h30 \n" +
                "- Áp dụng tất cả các ngày trong tuần \n" +
                "- Có áp dụng các ngày lễ, Tết \n" +
                "\n" +
                "Chi tiết địa điểm xem tại \"Điểm áp dụng\" \n" +
                "\n" +
                "Vui lòng bấm XÁC NHẬN ĐẶT CHỖ để nhận mã giảm giá \n" +
                "\n" +
                "LƯU Ý \n" +
                "- Chương trình chỉ áp dụng với khách dùng tại cửa hàng & mua mang về. Không áp dụng giao hàng \n" +
                "- Không áp dụng phụ thu \n" +
                "- Ưu đãi đã bao gồm VAT \n" +
                "- Không áp dụng đồng thời với các chương trình khác của The Alley \n" +
                "- Thông báo mã JAMJA ngay khi đến cửa hàng để được hướng dẫn nhận khuyến mãi \n" +
                "- Khách hàng được phép đến sớm hoặc muộn hơn 15 phút so với giờ hẹn đến \n" +
                "- Mã giảm giá không có giá trị quy đổi thành tiền mặt \n" +
                "\n" +
                "HOTLINE \n" +
                "- JAMJA: 1900 565 665 \n" +
                "\n" +
                "MẸO: Bấm \"Theo dõi\" thương hiệu để cập nhật những thay đổi về ưu đãi ngay tức thì.";
        final String dumbAdd = "6969696 Tran Duy Hung";
        holder.txtSalonName.setText(salonServices.get(position).getService().getServiceName());
//        holder.imgSalonThumb.setImageResource(salonServices.get(position).getThumbnail());
        holder.txtSalonAddress.setText("696969 HCM");
        holder.txtSaleValue.setText(" - " + salonServices.get(position).getDiscount().getDiscountValue());
        Picasso.with(mContext).
                load(salonServices.get(position).getSalon().getUrl())
                .into(holder.imgSalonThumb);

        // event when tap on a item
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass data to Detail salon activity
                Intent intent = new Intent(mContext, DetailSalonActivity.class);
                intent.putExtra("SalonService", salonServices.get(position).getService().getServiceName());
                intent.putExtra("SalonName", salonServices.get(position).getSalon().getName());
                intent.putExtra("Description", txtDumb);
                intent.putExtra("Thumbnail", salonServices.get(position).getSalon().getUrl());
                intent.putExtra("Address", dumbAdd);
//                intent.putExtra("ServiceListName",salonServices.get(position).getSalonServiceListName());
                // data need to be received in DetailSalonA
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return salonServices.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtSalonName;
        TextView txtSalonAddress;
        TextView txtSaleValue;
        ImageView imgSalonThumb;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtSalonName = itemView.findViewById(R.id.salon_service_name);
            txtSalonAddress = itemView.findViewById(R.id.salon_address);
            txtSaleValue = itemView.findViewById(R.id.txt_sale_value);
            imgSalonThumb = itemView.findViewById(R.id.salon_img);
            cardView = itemView.findViewById(R.id.card_view_salon_service);
        }
    }
}

