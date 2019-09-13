package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewNotifyAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewServiceByDiscountAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelAccount;
import capstone.sonnld.hairsalonbooking.model.ModelNotify;
import capstone.sonnld.hairsalonbooking.support.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationActivity extends AppCompatActivity {

    // api
    private HairSalonAPI hairSalonAPI;

    private RecyclerView recyclerView;
    int cusId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        // get data from mainA
        Intent intent = getIntent();
        cusId = intent.getExtras().getInt("cusId");

        recyclerView = findViewById(R.id.recycler_view_notify);
        recyclerView.setLayoutManager(new GridLayoutManager(NotificationActivity.this, 1));
        getAllNotify();
    }

    private void getAllNotify() {
        Call<List<ModelNotify>> listCall = hairSalonAPI.getAllNotifyByCusId(cusId);

        listCall.enqueue(new Callback<List<ModelNotify>>() {
            @Override
            public void onResponse(Call<List<ModelNotify>> call, Response<List<ModelNotify>> response) {
                ArrayList<ModelNotify> modelNotifies = (ArrayList<ModelNotify>) response.body();
                displayNotify(modelNotifies);
            }

            @Override
            public void onFailure(Call<List<ModelNotify>> call, Throwable t) {

            }
        });
    }

    private void displayNotify(ArrayList<ModelNotify> modelNotifies) {
        RecyclerViewNotifyAdapter viewAdapter
                = new RecyclerViewNotifyAdapter(NotificationActivity.this, modelNotifies);
        recyclerView.setAdapter(viewAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getAllNotify();
    }
}
