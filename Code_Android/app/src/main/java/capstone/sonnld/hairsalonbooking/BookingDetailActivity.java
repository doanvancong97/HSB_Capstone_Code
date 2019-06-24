package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSelectedServiceAdapter;
import capstone.sonnld.hairsalonbooking.model.SalonService;

public class BookingDetailActivity extends AppCompatActivity {

    private ReadMoreTextView txt_description;
    private RecyclerView recyclerView;

    private TextView txtTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        txt_description = findViewById(R.id.txt_description2);
        txtTotalPrice = findViewById(R.id.txt_total);

        String des1 = "ÁP DỤNG KHI DÙNG DỊCH VỤ TẠI CỬA HÀNG* \n" +
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
        txt_description.setText(des1);
        Intent intent = getIntent();
        ArrayList<SalonService> salonServices =
                (ArrayList<SalonService>) intent.getSerializableExtra("chkService");

        int totalPrice = 0;
        for(int i = 0; i < salonServices.size(); i++){
            String price = salonServices.get(i).getPrice();
            String discount = salonServices.get(i).getDiscount().getDiscountValue();
            int salePrice = getSalePrice(price,discount);
//            String sSalePrice = price.substring(0, price.length() - 1);
//            int nSalePrice = Integer.parseInt(sSalePrice);
            totalPrice += salePrice;
        }
        txtTotalPrice.setText("Tổng tiền là: " + totalPrice + "k");

        recyclerView = findViewById(R.id.recycler_selected_service);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        RecyclerViewSelectedServiceAdapter serviceAdapter =
                new RecyclerViewSelectedServiceAdapter(this,salonServices);
        recyclerView.setAdapter(serviceAdapter);


    }

    public int getSalePrice(String price,String discountValue){

        String sSalePrice = price.substring(0, price.length() - 1);
        int nSalePrice = Integer.parseInt(sSalePrice);
        int nDiscountValue = Integer.parseInt(discountValue);
        nSalePrice = nSalePrice - (nSalePrice * nDiscountValue / 100);

        return nSalePrice ;
    }

    public void unBooking(View view) {
        finish();
    }
}
