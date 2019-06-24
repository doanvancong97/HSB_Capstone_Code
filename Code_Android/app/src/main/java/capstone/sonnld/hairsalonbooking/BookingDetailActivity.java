package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSelectedServiceAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.dto.BookingDTO;
import capstone.sonnld.hairsalonbooking.dto.BookingDetailsDTO;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingDetailActivity extends AppCompatActivity {

    private ReadMoreTextView txt_description;
    private RecyclerView recyclerView;
    private Button btnAccept;


    private TextView txtTotalPrice;
    private HairSalonAPI hairSalonAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        txt_description = findViewById(R.id.txt_description2);
        txtTotalPrice = findViewById(R.id.txt_total);
        btnAccept = findViewById(R.id.btn_accept);

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
        for (int i = 0; i < salonServices.size(); i++) {
            String price = salonServices.get(i).getPrice();
            String discount = salonServices.get(i).getDiscount().getDiscountValue();
            int salePrice = getSalePrice(price, discount);
            totalPrice += salePrice;
        }
        txtTotalPrice.setText("Tổng tiền là: " + totalPrice + "k");

        recyclerView = findViewById(R.id.recycler_selected_service);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        RecyclerViewSelectedServiceAdapter serviceAdapter =
                new RecyclerViewSelectedServiceAdapter(this, salonServices);
        recyclerView.setAdapter(serviceAdapter);

        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        final ArrayList<BookingDetailsDTO> bookingDetailsDTOList = new ArrayList<>();
        for (int i = 0; i < salonServices.size(); i++) {
            int salonServiceID = salonServices.get(i).getSalonServiceId();
            int reviewId = 6;
            String serviceName = salonServices.get(i).getService().getServiceName();
            String status = "process";
            BookingDetailsDTO bookingDetailsDTO = new BookingDetailsDTO(salonServiceID, reviewId, serviceName, status);
            bookingDetailsDTOList.add(bookingDetailsDTO);
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBooking(bookingDetailsDTOList);
            }
        });


    }

    public void submitBooking(ArrayList<BookingDetailsDTO> dtoList) {
        BookingDTO bookingDTO = new BookingDTO(1,"Sonnnnnn","09999999","2019-12-19","12:12","process",dtoList);

        Call<BookingDTO> call = hairSalonAPI.postBooking(bookingDTO);

        call.enqueue(new Callback<BookingDTO>() {
            @Override
            public void onResponse(Call<BookingDTO> call, Response<BookingDTO> response) {
                Toast.makeText(BookingDetailActivity.this,response.code() + "",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BookingDTO> call, Throwable t) {
                Toast.makeText(BookingDetailActivity.this,t.getMessage() + "",Toast.LENGTH_LONG).show();
            }
        });
    }

    public int getSalePrice(String price, String discountValue) {

        String sSalePrice = price.substring(0, price.length() - 1);
        int nSalePrice = Integer.parseInt(sSalePrice);
        int nDiscountValue = Integer.parseInt(discountValue);
        nSalePrice = nSalePrice - (nSalePrice * nDiscountValue / 100);

        return nSalePrice;
    }

    public void unBooking(View view) {
        finish();
    }
}
