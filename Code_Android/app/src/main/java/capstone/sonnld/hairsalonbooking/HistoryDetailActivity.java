package capstone.sonnld.hairsalonbooking;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewBookedServiceAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelAccount;
import capstone.sonnld.hairsalonbooking.model.ModelBooking;
import capstone.sonnld.hairsalonbooking.model.ModelBookingDetail;
import capstone.sonnld.hairsalonbooking.support.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoryDetailActivity extends AppCompatActivity {

    // api
    private HairSalonAPI hairSalonAPI;

    // user detail
    private TextView txtUsername;

    private SessionManager sessionManager;
    private String mUserName;

    private RecyclerView recyclerView;
    private Button btnCancel;
    private Button btnReview;

    private TextView txtBookedDate;
    private TextView txtBookedTime;
    private TextView txtTotalPrice;
    private TextView txtAddress;
    private TextView txtDirection;

    private int bookingId;
    private int userID;
    private String salonName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        txtDirection = findViewById(R.id.txtDirection);
        txtTotalPrice = findViewById(R.id.txt_total);
        btnCancel = findViewById(R.id.btn_cancel);
        txtBookedDate = findViewById(R.id.txt_booked_date);
        txtBookedTime = findViewById(R.id.txt_booked_time);
        txtAddress = findViewById(R.id.txt_address);
        txtUsername = findViewById(R.id.txt_username);
        btnReview = findViewById(R.id.btn_review);

        // get session
        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLogin()) {
            HashMap<String, String> user = sessionManager.getUserDetail();
            mUserName = user.get(sessionManager.getUSERNAME());
            initUserDetail();
        }

        // get data from RecyclerViewBookingHistoryAdapter
        final Intent intent = getIntent();
        String bookedDate = intent.getExtras().getString("BookedDate");
        String bookedTime = intent.getExtras().getString("BookedTime");
        bookingId = intent.getExtras().getInt("BookingId");
        salonName = intent.getExtras().getString("SalonName");
        String[] bookedDateArr = bookedDate.split("-");
        String[] bookedTimeArr = bookedTime.split(":");

        String status = intent.getExtras().getString("BookingStatus");
        String address = intent.getExtras().getString("ModelAddress");
        ArrayList<ModelBookingDetail> selectedService =
                (ArrayList<ModelBookingDetail>) intent.getSerializableExtra("SelectedService");

        int totalPrice = 0;
        for (int i = 0; i < selectedService.size(); i++) {
            String price = selectedService.get(i).getModelSalonService().getPrice();
            String discount = selectedService.get(i).getModelSalonService().getModelDiscount().getDiscountValue();
            int salePrice = getSalePrice(price, discount);
            totalPrice += salePrice;
        }

        txtTotalPrice.setText("Tổng tiền là: " + totalPrice + "k");
        txtBookedDate.setText(bookedDateArr[2]+"/"+bookedDateArr[1]+"/"+bookedDateArr[0]);
        txtBookedTime.setText(bookedTimeArr[0]+":"+bookedTimeArr[1]);
        txtAddress.setText(address);

        //recycler view setup
        recyclerView = findViewById(R.id.recycler_selected_service);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        RecyclerViewBookedServiceAdapter serviceAdapter =
                new RecyclerViewBookedServiceAdapter(this, selectedService);
        recyclerView.setAdapter(serviceAdapter);

        // btn direction to salon
        txtDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri gmmIntentUri = Uri.parse("google.navigation:q="+ txtAddress.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        // setup btn cancel
        if(!status.equals("process")){
            btnCancel.setVisibility(View.GONE);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryDetailActivity.this);
                builder.setTitle("Hủy đơn đặt chỗ")
                        .setMessage("Bạn có muốn hủy đơn đặt chỗ không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cancelBooking();
                            }
                        })
                        .setNegativeButton("Không", null);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        // setup btn review
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openReviewActivity = new Intent(HistoryDetailActivity.this, ReviewActivity.class);
                openReviewActivity.putExtra("userId",userID);
                openReviewActivity.putExtra("salonName",salonName);
                openReviewActivity.putExtra("bookingId",bookingId);

                startActivity(openReviewActivity);
            }
        });
    }

    private void cancelBooking() {
        Call<ModelBooking> call = hairSalonAPI.cancelBooking(bookingId);
        call.enqueue(new Callback<ModelBooking>() {
            @Override
            public void onResponse(Call<ModelBooking> call, Response<ModelBooking> response) {
                if(response.code() == 200){
                    Toast.makeText(HistoryDetailActivity.this, "Hủy đơn đặt chỗ thành công",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HistoryDetailActivity.this, HistoryActivity.class);
                    intent.putExtra("USER_ID",userID);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ModelBooking> call, Throwable t) {

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
        Call<ModelAccount> call = hairSalonAPI.getUserDetail(mUserName);
        call.enqueue(new Callback<ModelAccount>() {
            @Override
            public void onResponse(Call<ModelAccount> call, Response<ModelAccount> response) {
                ModelAccount currentAcc = response.body();
                String fullName = currentAcc.getFullname();
                txtUsername.setText(fullName);
                userID = currentAcc.getUserId();
            }

            @Override
            public void onFailure(Call<ModelAccount> call, Throwable t) {

            }
        });
    }
}
