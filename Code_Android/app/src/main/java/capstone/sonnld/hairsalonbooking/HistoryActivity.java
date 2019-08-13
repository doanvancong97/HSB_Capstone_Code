package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewBookingHistoryAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelBooking;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoryActivity extends AppCompatActivity {

    private HairSalonAPI hairSalonAPI;
    private RecyclerView recyclerViewHistory;

    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        //get data from HistoryDetailActivity/ MainActivity
        Intent intent = getIntent();
        userID = intent.getExtras().getInt("USER_ID");
        Toast.makeText(this, "id="+userID, Toast.LENGTH_SHORT).show();

        recyclerViewHistory=findViewById(R.id.recycler_view_history);
        recyclerViewHistory.setLayoutManager(new GridLayoutManager(this, 1));
        getAllBookingHistory(userID);


    }

    private void getAllBookingHistory( int userID) {
        Call<ArrayList<ModelBooking>> call = hairSalonAPI.getBookingHistoryByUserID(userID);

        call.enqueue(new Callback<ArrayList<ModelBooking>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelBooking>> call, Response<ArrayList<ModelBooking>> response) {
                displayBookingByUserID(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ModelBooking>> call, Throwable t) {

            }
        });
    }

    private void displayBookingByUserID( ArrayList<ModelBooking> listModelBookingHistory) {
        RecyclerViewBookingHistoryAdapter adapter
                = new RecyclerViewBookingHistoryAdapter(HistoryActivity.this, listModelBookingHistory);
        recyclerViewHistory.setAdapter(adapter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getAllBookingHistory(userID);
    }
}
