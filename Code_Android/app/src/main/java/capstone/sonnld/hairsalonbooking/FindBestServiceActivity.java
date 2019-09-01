package capstone.sonnld.hairsalonbooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewBestServiceAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSalonByRatingAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelSalon;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FindBestServiceActivity extends AppCompatActivity {

    // api
    private HairSalonAPI hairSalonAPI;

    //views
    private Button btnSearch;
    private EditText edtSearch;
    private RecyclerView recyclerViewBestService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_best_service);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        // init views
        recyclerViewBestService = findViewById(R.id.recycler_best_service);
        btnSearch = findViewById(R.id.btn_search);
        edtSearch = findViewById(R.id.edt_search);

        recyclerViewBestService.setLayoutManager(new GridLayoutManager(FindBestServiceActivity.this,1));

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchValue = edtSearch.getText().toString();
                searchService(searchValue);
            }
        });
    }

    private void searchService(String searchVal) {
        Call<ArrayList<ModelSalonService>> listCall = hairSalonAPI.searchServiceByName(searchVal);
        listCall.enqueue(new Callback<ArrayList<ModelSalonService>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelSalonService>> call, Response<ArrayList<ModelSalonService>> response) {
                // get service list
                ArrayList<ModelSalonService> serviceArrayList = response.body();

                // sort by discount
                Collections.sort(serviceArrayList, new Comparator<ModelSalonService>() {
                    @Override
                    public int compare(ModelSalonService o1, ModelSalonService o2) {
                        return Integer.compare(o2.getModelDiscount().getDiscountValue(), o1.getModelDiscount().getDiscountValue());
                    }

                });
                // add point
                for(int i = 0; i < serviceArrayList.size(); i++){
                    serviceArrayList.get(i).setDiscountPoint(serviceArrayList.size() - i);
                }

                // sort by rating
                Collections.sort(serviceArrayList, new Comparator<ModelSalonService>() {
                    @Override
                    public int compare(ModelSalonService o1, ModelSalonService o2) {
                        return Float.compare(o2.getModelSalon().getAverageRating(), o1.getModelSalon().getAverageRating());
                    }

                });
                // add point
                for(int i = 0; i < serviceArrayList.size(); i++){
                    serviceArrayList.get(i).setRatingPoint(serviceArrayList.size() - i);
                }

                // caculate avgPoint
                for(int i = 0; i < serviceArrayList.size(); i++){
                    double avgPoint = (serviceArrayList.get(i).getDiscountPoint()
                            + serviceArrayList.get(i).getRatingPoint()) / 2;
                    serviceArrayList.get(i).setAvgPoint(avgPoint);
                }
                // sort by avg point
                Collections.sort(serviceArrayList, new Comparator<ModelSalonService>() {
                    @Override
                    public int compare(ModelSalonService o1, ModelSalonService o2) {
                        return Double.compare(o2.getAvgPoint(), o1.getAvgPoint());
                    }

                });
                // add 3 best service
                ArrayList<ModelSalonService> bestServices = new ArrayList<>();
                for (int i = 0; i < 3; i++){
                    bestServices.add(serviceArrayList.get(i));
                }
                displayServices(bestServices);

            }

            @Override
            public void onFailure(Call<ArrayList<ModelSalonService>> call, Throwable t) {

            }
        });
    }

    private void displayServices(ArrayList<ModelSalonService> serviceArrayList) {
        RecyclerViewBestServiceAdapter recyclerViewSalonByRatingAdapter =
                new RecyclerViewBestServiceAdapter(FindBestServiceActivity.this,serviceArrayList);
        recyclerViewBestService.setAdapter(recyclerViewSalonByRatingAdapter);
    }
}
