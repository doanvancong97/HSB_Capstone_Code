package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewBookedServiceAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSelectedServiceAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.Account;
import capstone.sonnld.hairsalonbooking.model.BookingDetail;
import capstone.sonnld.hairsalonbooking.model.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoryDetailActivity extends AppCompatActivity {

    // api
    private HairSalonAPI hairSalonAPI;

    // user detail
    private TextView txtUsername;
    private int userId;

    private SessionManager sessionManager;
    private String mUserName;

    private RecyclerView recyclerView;
    private Button btnAccept;

    private TextView txtBookedDate;
    private TextView txtBookedTime;
    private TextView txtTotalPrice;
    private TextView txtAddress;
    private TextView txtDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        txtDirection = findViewById(R.id.txtDirection);
        txtTotalPrice = findViewById(R.id.txt_total);
        btnAccept = findViewById(R.id.btn_accept);
        txtBookedDate = findViewById(R.id.txt_booked_date);
        txtBookedTime = findViewById(R.id.txt_booked_time);
        txtAddress = findViewById(R.id.txt_address);
        txtUsername = findViewById(R.id.txt_username);

        // get session
        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLogin()) {

            HashMap<String, String> user = sessionManager.getUserDetail();
            mUserName = user.get(sessionManager.getUSERNAME());
            initUserDetail();
        }


        Intent intent = getIntent();
        String bookedDate = intent.getExtras().getString("BookedDate");
        String bookedTime = intent.getExtras().getString("BookedTime");
        String[] bookedDateArr = bookedDate.split("-");
        String[] bookedTimeArr = bookedTime.split(":");

        String address = intent.getExtras().getString("Address");
        ArrayList<BookingDetail> selectedService =
                (ArrayList<BookingDetail>) intent.getSerializableExtra("SelectedService");

        int totalPrice = 0;
        for (int i = 0; i < selectedService.size(); i++) {
            String price = selectedService.get(i).getSalonService().getPrice();
            String discount = selectedService.get(i).getSalonService().getDiscount().getDiscountValue();
            int salePrice = getSalePrice(price, discount);
            totalPrice += salePrice;
        }

        txtTotalPrice.setText("Tổng tiền là: " + totalPrice + "k");
        txtBookedDate.setText(bookedDateArr[2]+"/"+bookedDateArr[1]+"/"+bookedDateArr[0]);
        txtBookedTime.setText(bookedTimeArr[0]+":"+bookedTimeArr[1]);
        txtAddress.setText(address);

        recyclerView = findViewById(R.id.recycler_selected_service);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        RecyclerViewBookedServiceAdapter serviceAdapter =
                new RecyclerViewBookedServiceAdapter(this, selectedService);
        recyclerView.setAdapter(serviceAdapter);

        txtDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri gmmIntentUri = Uri.parse("google.navigation:q="+ txtAddress.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
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

    private void initUserDetail() {
        Call<Account> call = hairSalonAPI.getUserDetail(mUserName);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account currentAcc = response.body();
                String fullName = currentAcc.getFullname();
                txtUsername.setText(fullName);
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
    }
}
