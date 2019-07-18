package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSelectedServiceAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.dto.BookingDTO;
import capstone.sonnld.hairsalonbooking.dto.BookingDetailsDTO;
import capstone.sonnld.hairsalonbooking.model.GeoPoint;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import capstone.sonnld.hairsalonbooking.model.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingDetailActivity extends AppCompatActivity {

    private ReadMoreTextView txtDescription;
    private RecyclerView recyclerView;
    private Button btnAccept;

    private TextView txtBookedDate;
    private TextView txtBookedTime;
    private TextView txtTotalPrice;
    private TextView txtAddress;
    private  TextView txtDirection;

    private ArrayList<BookingDetailsDTO> bookingDetailsDTOList = new ArrayList<>();
    private ArrayList<SalonService> salonServices;

    // api
    private HairSalonAPI hairSalonAPI;


    // user detail
    private TextView txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        txtDirection = findViewById(R.id.txtDirection);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        txtDescription = findViewById(R.id.txt_description);
        txtTotalPrice = findViewById(R.id.txt_total);
        btnAccept = findViewById(R.id.btn_accept);
        txtBookedDate = findViewById(R.id.txt_booked_date);
        txtBookedTime = findViewById(R.id.txt_booked_time);
        txtAddress = findViewById(R.id.txt_address);
        txtUsername = findViewById(R.id.txt_username);




        // get data from detail salon/ detail service activity
        Intent intent = getIntent();
        salonServices = (ArrayList<SalonService>) intent.getSerializableExtra("chkService");
        String bookedDate = intent.getExtras().getString("bookedDate");
        String bookedTime = intent.getExtras().getString("bookedTime");
        final String address = intent.getExtras().getString("salonAddress");
        String fullname = intent.getExtras().getString("fullname");
        String des = intent.getExtras().getString("description");

        // setup value
        int totalPrice = 0;
        for (int i = 0; i < salonServices.size(); i++) {
            String price = salonServices.get(i).getPrice();
            String discount = salonServices.get(i).getDiscount().getDiscountValue();
            int salePrice = getSalePrice(price, discount);
            totalPrice += salePrice;
        }
        txtTotalPrice.setText("Tổng tiền là: " + totalPrice + "k");
        txtBookedDate.setText(bookedDate);
        txtBookedTime.setText(bookedTime);
        txtAddress.setText(address);
        txtUsername.setText(fullname);
        txtDescription.setText(des);

        txtDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri gmmIntentUri = Uri.parse("google.navigation:q="+address);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


        recyclerView = findViewById(R.id.recycler_selected_service);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        RecyclerViewSelectedServiceAdapter serviceAdapter =
                new RecyclerViewSelectedServiceAdapter(this, salonServices);
        recyclerView.setAdapter(serviceAdapter);

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
                submitBooking();
            }
        });


    }
    

    public void submitBooking() {
        BookingDTO bookingDTO = new BookingDTO(1,
                "Sonnnnnn", "09999999",
                "2019-12-19", "12:12", "process", bookingDetailsDTOList);

        Call<BookingDTO> call = hairSalonAPI.postBooking(bookingDTO);

        call.enqueue(new Callback<BookingDTO>() {
            @Override
            public void onResponse(Call<BookingDTO> call, Response<BookingDTO> response) {
                Toast.makeText(BookingDetailActivity.this,
                        "Cảm ơn quý khách đã sử dụng dịch vụ.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BookingDetailActivity.this, ReceiptActivity.class);
                intent.putExtra("fullname",txtUsername.getText().toString());
                intent.putExtra("bookedDate",txtBookedDate.getText().toString());
                intent.putExtra("bookedTime",txtBookedTime.getText().toString());
                intent.putExtra("address",txtAddress.getText().toString());
                intent.putExtra("salonServices",salonServices);
                intent.putExtra("totalPrice",txtTotalPrice.getText().toString());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<BookingDTO> call, Throwable t) {
                Toast.makeText(BookingDetailActivity.this, t.getMessage() + "", Toast.LENGTH_LONG).show();
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
