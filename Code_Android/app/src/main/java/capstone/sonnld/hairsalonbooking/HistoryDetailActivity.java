package capstone.sonnld.hairsalonbooking;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewBookedServiceAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.dto.RatingDTO;
import capstone.sonnld.hairsalonbooking.model.ModelAccount;
import capstone.sonnld.hairsalonbooking.model.ModelBooking;
import capstone.sonnld.hairsalonbooking.model.ModelBookingDetail;
import capstone.sonnld.hairsalonbooking.model.ModelNotify;
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
    private LinearLayout lnReview;

    private TextView txtBookedDate;
    private TextView txtBookedTime;
    private TextView txtTotalPrice;
    private TextView txtAddress;
    private TextView txtStatus;
    private TextView txtDirection;
    private Button btnMain;

    private int bookingId;
    private int userID;
    private String salonName;

    //view value
    private RatingBar ratingBar;
    private Button btnSubmit;
    private EditText edtComment;
    private TextView txtSalonName;
    // value to submit
    private int rating;
    private String comment;
    private Date createdDate;

    String bookedDate;
    String bookedTime;
    String status;
    String address;
    String isRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        //find view
        txtDirection = findViewById(R.id.txtDirection);
        txtTotalPrice = findViewById(R.id.txt_total);
        btnCancel = findViewById(R.id.btn_cancel);
        txtBookedDate = findViewById(R.id.txt_booked_date);
        txtBookedTime = findViewById(R.id.txt_booked_time);
        txtAddress = findViewById(R.id.txt_address);
        txtUsername = findViewById(R.id.txt_username);
        btnReview = findViewById(R.id.btn_review);
        lnReview = findViewById(R.id.rating_layout);
        ratingBar = findViewById(R.id.rating_bar);
        btnSubmit = findViewById(R.id.btn_submit);
        txtSalonName = findViewById(R.id.txt_salon_name);
        btnMain = findViewById(R.id.btn_go_to_main);
        txtStatus = findViewById(R.id.txt_status);
        edtComment = findViewById(R.id.edt_comment);

        // get session
        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLogin()) {
            HashMap<String, String> user = sessionManager.getUserDetail();
            mUserName = user.get(sessionManager.getUSERNAME());
            initUserDetail();
        }

        // get data from RecyclerViewBookingHistoryAdapter/ Firebase service
        final Intent intent = getIntent();
        bookedDate = intent.getExtras().getString("BookedDate");
        bookedTime = intent.getExtras().getString("BookedTime");
        bookingId = intent.getExtras().getInt("BookingId");
        salonName = intent.getExtras().getString("SalonName");
        status = intent.getExtras().getString("BookingStatus");
        address = intent.getExtras().getString("ModelAddress");
        if(intent.getExtras().getString("Seen") == null){
            isRead = "no";
        }else{
            isRead = intent.getExtras().getString("Seen");
        }

        setupViews();

    }

    private void setupViews() {
        Call<ArrayList<ModelBookingDetail>> call = hairSalonAPI.getBookingDetailByBookingId(bookingId);

        call.enqueue(new Callback<ArrayList<ModelBookingDetail>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelBookingDetail>> call, Response<ArrayList<ModelBookingDetail>> response) {

                ArrayList<ModelBookingDetail> selectedService = response.body();

                int totalPrice = 0;
                for (int i = 0; i < selectedService.size(); i++) {
                    String price = selectedService.get(i).getModelSalonService().getPrice() + "";
                    String discount = selectedService.get(i).getModelSalonService().getModelDiscount().getDiscountValue() + "";
                    int salePrice = getSalePrice(price, discount);
                    totalPrice += salePrice;
                }

                //update notify is read status
                updateIsRead();

                //set data to view
                String[] bookedDateArr = bookedDate.split("-");
                String[] bookedTimeArr = bookedTime.split(":");
                txtTotalPrice.setText(totalPrice + "k");
                txtBookedDate.setText(bookedDateArr[2] + "/" + bookedDateArr[1] + "/" + bookedDateArr[0]);
                txtBookedTime.setText(bookedTimeArr[0] + ":" + bookedTimeArr[1]);
                txtAddress.setText(address);
                txtSalonName.setText("Đánh giá salon " + salonName);
                if (status.equals("1process")) {
                    txtStatus.setText("Đang xử lý");
                    txtStatus.setTextColor(Color.BLUE);
                }
                if (status.equals("2cancel")) {
                    txtStatus.setText("Bạn đã hủy");
                    txtStatus.setTextColor(Color.RED);
                }
                if (status.equals("3salonCancel")) {
                    txtStatus.setText("Bị hủy bởi salon");
                    txtStatus.setTextColor(Color.RED);
                }
                if (status.equals("4finish")) {
                    txtStatus.setText("Đã hoàn thành");
                    txtStatus.setTextColor(Color.GREEN);
                }
                //recycler view setup
                recyclerView = findViewById(R.id.recycler_selected_service);
                recyclerView.setLayoutManager(new GridLayoutManager(HistoryDetailActivity.this, 1));
                RecyclerViewBookedServiceAdapter serviceAdapter =
                        new RecyclerViewBookedServiceAdapter(HistoryDetailActivity.this, selectedService);
                recyclerView.setAdapter(serviceAdapter);

                // btn direction to salon

                txtDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + txtAddress.getText().toString());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });

                // back to main activity
                btnMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentMain = new Intent(HistoryDetailActivity.this, MainActivity.class);
                        finish();
                        startActivity(intentMain);

                    }
                });

                // setup btn cancel
                if (!status.equals("1process")) {
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


                // setup btn review/review layout
                btnReview.setVisibility(View.GONE);
                lnReview.setVisibility(View.GONE);
                if (status.equals("4finish")) {
                    lnReview.setVisibility(View.VISIBLE);
                }

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        submitReview();
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<ModelBookingDetail>> call, Throwable t) {

            }
        });
    }

    private void updateIsRead() {
        Call<ModelNotify> modelNotifyCall = hairSalonAPI.updateIsRead(bookingId);
        modelNotifyCall.enqueue(new Callback<ModelNotify>() {
            @Override
            public void onResponse(Call<ModelNotify> call, Response<ModelNotify> response) {


            }

            @Override
            public void onFailure(Call<ModelNotify> call, Throwable t) {

            }
        });
    }


    public void submitReview() {
        getSalonIdByBookingId();
    }

    public void getSalonIdByBookingId() {
        Call<Integer> integerCall = hairSalonAPI.getSalonIdByBookingId(bookingId);
        integerCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int salonId = response.body();
                rating = (int) ratingBar.getRating();
                comment = edtComment.getText().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                createdDate = new Date(System.currentTimeMillis());
                String sCreatedDate = formatter.format(createdDate);

                RatingDTO ratingDTO = new RatingDTO();
                ratingDTO.setBookingId(bookingId);
                ratingDTO.setComment(comment);
                ratingDTO.setCreatedDate(sCreatedDate);
                ratingDTO.setRating(rating);
                ratingDTO.setUserId(userID);
                ratingDTO.setSalonId(salonId);

                Call<ModelBooking> modelBookingCall = hairSalonAPI.addReviewToBooking(ratingDTO);
                modelBookingCall.enqueue(new Callback<ModelBooking>() {
                    @Override
                    public void onResponse(Call<ModelBooking> call, Response<ModelBooking> response) {
                        if (response.code() == 200) {
                            Toast.makeText(HistoryDetailActivity.this, "Đã đăng tải bình luận", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(HistoryDetailActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelBooking> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void cancelBooking() {
        Call<ModelBooking> call = hairSalonAPI.cancelBooking(bookingId);
        call.enqueue(new Callback<ModelBooking>() {
            @Override
            public void onResponse(Call<ModelBooking> call, Response<ModelBooking> response) {
                if (response.code() == 200) {
                    Toast.makeText(HistoryDetailActivity.this, "Hủy đơn đặt chỗ thành công",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HistoryDetailActivity.this, HistoryActivity.class);
                    intent.putExtra("USER_ID", userID);
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

        int nSalePrice = Integer.parseInt(price);
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
