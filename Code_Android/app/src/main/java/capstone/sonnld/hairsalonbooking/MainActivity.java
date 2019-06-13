package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import capstone.sonnld.hairsalonbooking.API.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSalonByDiscountAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSalonByRatingAdapter;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import capstone.sonnld.hairsalonbooking.model.Suggesttion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    TextView txtTestLogin;

    private HairSalonAPI hairSalonAPI;
    private List<SalonService> salonServiceList;


    MaterialSpinner spinnerLocation;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTestLogin = findViewById(R.id.txtTestLogin);
        checkLoginByUser();
        spinnerLocation = findViewById(R.id.spinnerLocation);
        spinnerLocation.setItems("HỒ CHÍ MINH", "HÀ NỘI", "ĐÀ NẴNG");


        //setup tool bar
        mToolbar = findViewById(R.id.nav_action_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //setup sideBar
        mDrawerLayout  = findViewById(R.id.drawerLayout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();


        //end setup sideBar


        salonServiceList = new ArrayList<>();
        //init retro
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.26:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        hairSalonAPI = retrofit.create(HairSalonAPI.class);
        Call<List<SalonService>> callServiceByDV = hairSalonAPI.getAllServiceByDiscountValue();
        callServiceByDV.enqueue(new Callback<List<SalonService>>() {
            @Override
            public void onResponse(Call<List<SalonService>> call, Response<List<SalonService>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Code: " + response.code(),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                salonServiceList = response.body();
                RecyclerView recyclerView = findViewById(R.id.recycler_view_salon);
                RecyclerViewSalonByDiscountAdapter viewAdapter = new RecyclerViewSalonByDiscountAdapter(MainActivity.this, salonServiceList);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                recyclerView.setAdapter(viewAdapter);
            }

            @Override
            public void onFailure(Call<List<SalonService>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Code: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });


        // recycler view for best service

        Call<List<SalonService>> callServiceByRating = hairSalonAPI.getAllServiceByRating();
        callServiceByRating.enqueue(new Callback<List<SalonService>>() {
            @Override
            public void onResponse(Call<List<SalonService>> call, Response<List<SalonService>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Code: " + response.code(),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                salonServiceList = response.body();
                RecyclerView recyclerViewBestService = findViewById(R.id.recycler_view_good_service);
                RecyclerViewSalonByRatingAdapter viewNewestAdapter = new RecyclerViewSalonByRatingAdapter
                        (MainActivity.this, salonServiceList);
                recyclerViewBestService.setLayoutManager(new GridLayoutManager
                        (MainActivity.this, 1));
                recyclerViewBestService.setAdapter(viewNewestAdapter);
            }

            @Override
            public void onFailure(Call<List<SalonService>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Code: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mActionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }






    public void checkLoginByUser() {
        Intent intent = this.getIntent();


        if (intent.getStringExtra("username") != null) {
            txtTestLogin.setText("Welcome: " + intent.getStringExtra("username"));
        } else {


            txtTestLogin.setText("");
        }


    }


    public void clickToRedirectToSearch(View view) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void clickToRedirectToLogin(View view){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

    }


}
