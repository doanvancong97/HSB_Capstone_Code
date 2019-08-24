package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.dto.RatingDTO;
import capstone.sonnld.hairsalonbooking.model.ModelBooking;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewActivity extends AppCompatActivity {

    //view value
    private RatingBar ratingBar;
    private Button btnSubmit;

    private TextView txtHeading;
    private EditText edtComment;

    // value to submit
    private int bookingId;
    private int userId;
    private int rating;
    private String comment;
    private Date createdDate;
    // api
    private HairSalonAPI hairSalonAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        // init view
        ratingBar = findViewById(R.id.rating_bar);

        btnSubmit = findViewById(R.id.btn_submit);

        edtComment = findViewById(R.id.edt_comment);
        txtHeading = findViewById(R.id.txt_heading);

        // get value from HistoryDetailActivity
        Intent intent = getIntent();
        bookingId = intent.getExtras().getInt("bookingId");
        userId = intent.getExtras().getInt("userId");
        String salonName = intent.getExtras().getString("salonName");

        txtHeading.setText("Đánh giá "+salonName);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitReview();
            }
        });
    }

    public void submitReview(){
        getSalonIdByBookingId();
    }

    public void getSalonIdByBookingId(){
        Call<Integer> integerCall = hairSalonAPI.getSalonIdByBookingId(bookingId);
        integerCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int salonId = response.body();
                rating = (int) ratingBar.getRating();
                comment = edtComment.getText().toString();
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                createdDate = new Date(System.currentTimeMillis());
                String sCreatedDate = formatter.format(createdDate);

                RatingDTO ratingDTO = new RatingDTO();
                ratingDTO.setBookingId(bookingId);
                ratingDTO.setComment(comment);
                ratingDTO.setCreatedDate(sCreatedDate);
                ratingDTO.setRating(rating);
                ratingDTO.setUserId(userId);
                ratingDTO.setSalonId(salonId);

                Call<ModelBooking> modelBookingCall = hairSalonAPI.addReviewToBooking(ratingDTO);
                modelBookingCall.enqueue(new Callback<ModelBooking>() {
                    @Override
                    public void onResponse(Call<ModelBooking> call, Response<ModelBooking> response) {
                        if(response.code() == 200){
                            Toast.makeText(ReviewActivity.this, "Đã đăng tải bình luận",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ReviewActivity.this, MainActivity.class);
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
}
