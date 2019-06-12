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

import java.util.ArrayList;
import java.util.List;

import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
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

    private String BASE_URL = "http://192.168.1.6:8080/api/";


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private Toolbar mToolbar;
    TextView txtTestLogin;

    private HairSalonAPI hairSalonAPI;
    private List<SalonService> salonServiceList;

    private FloatingSearchView floatingSearchView;
    private List<Suggesttion> mSuggestions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        txtTestLogin = findViewById(R.id.txtTestLogin);
//        checkLoginByUser();
        initData();
        final FloatingSearchView searchView = (FloatingSearchView) findViewById(R.id.floating_search_view);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    searchView.clearSuggestions();
                } else {
                    searchView.showProgress();
                    searchView.swapSuggestions(getSuggestion(newQuery));
                    searchView.hideProgress();
                }
            }
        });
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                searchView.showProgress();
                searchView.swapSuggestions(getSuggestion(searchView.getQuery()));
                searchView.hideProgress();
            }

            @Override
            public void onFocusCleared() {

            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                Suggesttion suggestion = (Suggesttion) searchSuggestion;
                Toast.makeText(getApplicationContext(), "Bạn vừa tìm " + suggestion.getBody(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });


        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerLayout.requestFocus();


        //setup tool bar
        mToolbar = findViewById(R.id.nav_action_bar);
        setSupportActionBar(mToolbar);


        //setup sideBar(left menu)

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //end setup sideBar


        salonServiceList = new ArrayList<>();
        //init retro

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);
        //

        // getAllServiceByDiscountValue API
        Call<List<SalonService>> callServiceByDV = hairSalonAPI.getAllServiceByDiscountValue();
        callServiceByDV.enqueue(new Callback<List<SalonService>>() {
            @Override
            public void onResponse(Call<List<SalonService>> call, Response<List<SalonService>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this,"Code: " + response.code(),
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
                Toast.makeText(MainActivity.this,"Code: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });






        Call<List<SalonService>> callServiceByRating = hairSalonAPI.getAllServiceByRating();
        callServiceByRating.enqueue(new Callback<List<SalonService>>() {
            @Override
            public void onResponse(Call<List<SalonService>> call, Response<List<SalonService>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this,"Code: " + response.code(),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                salonServiceList = response.body();
                RecyclerView recyclerViewNewest = findViewById(R.id.recycler_view_good_service);
                RecyclerViewSalonByRatingAdapter viewNewestAdapter = new RecyclerViewSalonByRatingAdapter
                        (MainActivity.this, salonServiceList);
                recyclerViewNewest.setLayoutManager(new GridLayoutManager
                        (MainActivity.this, 1));
                recyclerViewNewest.setAdapter(viewNewestAdapter);
            }

            @Override
            public void onFailure(Call<List<SalonService>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Code: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void clickToRedirectToLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void initData() {


        mSuggestions.add(new Suggesttion("4rau Barber "));
        mSuggestions.add(new Suggesttion("Cắt Tóc Sài Gòn"));
        mSuggestions.add(new Suggesttion("SunShine"));
        mSuggestions.add(new Suggesttion("FreeStyle Salon"));
        mSuggestions.add(new Suggesttion("Tony Hair Salon"));


    }

    private List<Suggesttion> getSuggestion(String query) {
        List<Suggesttion> suggestions = new ArrayList<>();
        for (Suggesttion suggestion : mSuggestions) {
            if (suggestion.getBody().toLowerCase().contains(query.toLowerCase())) {
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }

    public void checkLoginByUser() {
        Intent intent = this.getIntent();


        if (intent.getStringExtra("phonenumber") != null) {
            txtTestLogin.setText("Welcome: " + intent.getStringExtra("phonenumber"));
        } else {


            txtTestLogin.setText("");
        }


    }


}
