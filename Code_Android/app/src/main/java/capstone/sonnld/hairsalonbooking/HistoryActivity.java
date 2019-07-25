package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewBookingHistoryAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.Booking;
import capstone.sonnld.hairsalonbooking.model.BookingDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoryActivity extends AppCompatActivity {

    private HairSalonAPI hairSalonAPI;
    RecyclerView recyclerViewHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        Intent intent = getIntent();
        int userID= intent.getExtras().getInt("USER_ID");
        Toast.makeText(this, "id="+userID, Toast.LENGTH_SHORT).show();

        recyclerViewHistory=findViewById(R.id.recycler_view_history);
        recyclerViewHistory.setLayoutManager(new GridLayoutManager(this, 1));
        getAllBookingHistory(userID);


    }

    private void getAllBookingHistory( int userID) {
        Call<ArrayList<Booking>> call = hairSalonAPI.getBookingHistoryByUserID(userID);

        call.enqueue(new Callback<ArrayList<Booking>>() {
            @Override
            public void onResponse(Call<ArrayList<Booking>> call, Response<ArrayList<Booking>> response) {
                displayBookingByUserID(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Booking>> call, Throwable t) {

            }
        });
    }

    private void displayBookingByUserID( ArrayList<Booking> listBookingHistory) {
        RecyclerViewBookingHistoryAdapter adapter
                = new RecyclerViewBookingHistoryAdapter(HistoryActivity.this,listBookingHistory);
        recyclerViewHistory.setAdapter(adapter);

    }
}
