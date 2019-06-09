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

import capstone.sonnld.hairsalonbooking.API.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewNewestAdapter;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import capstone.sonnld.hairsalonbooking.model.Suggesttion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

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
        txtTestLogin = findViewById(R.id.txtTestLogin);
        checkLoginByUser();
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


        //setup sideBar

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //end setup sideBar

//        String des1 = "ÁP DỤNG KHI DÙNG DỊCH VỤ TẠI CỬA HÀNG* \n" +
//                "\n" +
//                "- Giảm 20% tổng hóa đơn áp dụng cho tất cả các dịch vụ \n" +
//                "- Áp dụng cho khách hàng nữ \n" +
//                "- Mỗi mã ưu đãi đổi được nhiều suất trong suốt chương trình \n" +
//                "- Khách hàng có thể lấy nhiều mã trong suốt chương trình \n" +
//                "\n" +
//                "THỜI GIAN ÁP DỤNG \n" +
//                "- Khung giờ: 9h30 - 19h00\t\n" +
//                "- Áp dụng tất cả các ngày trong tuần \n" +
//                "- Không áp dụng các ngày lễ, Tết: 30/4, 1/5 \n" +
//                "\n" +
//                "Chi tiết địa điểm xem tại \"Điểm áp dụng\" \n" +
//                "\n" +
//                "Vui lòng bấm XÁC NHẬN ĐẶT CHỖ để nhận mã giảm giá \n" +
//                "\n" +
//                "LƯU Ý \n" +
//                "- Chương trình chỉ áp dụng với khách dùng dịch vụ tại cửa hàng \n" +
//                "- Không áp dụng đồng thời với các chương trình khác của MIA.Nails & Cafe \n" +
//                "- Không áp dụng phụ thu \n" +
//                "- Ưu đãi chưa bao gồm VAT \n" +
//                "- Khách hàng được phép đến sớm hoặc muộn hơn 15 phút so với giờ hẹn đến \n" +
//                "- Mã giảm giá không có giá trị quy đổi thành tiền mặt ";
//
//        String shop1 = "https://cdn.jamja.vn/blog/wp-content/uploads/2019/01/4RAU-Barber-SHOP.jpg";
//        String shop2 = "https://cdn.jamja.vn/blog/wp-content/uploads/2019/01/Tiem-Barber-Shop-Vu-Tri.jpg";
//        String shop3 = "https://cdn.jamja.vn/blog/wp-content/uploads/2019/01/Tony-Barber-House.jpg";
//        String shop4 = "https://cdn.jamja.vn/blog/wp-content/uploads/2019/01/Tiem-Barber-Shop-Vu-Tri-2.jpg";

        salonServiceList = new ArrayList<>();
        //init retro
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.4:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        hairSalonAPI = retrofit.create(HairSalonAPI.class);
        Call<List<SalonService>> listCall = hairSalonAPI.getAllService();
        listCall.enqueue(new Callback<List<SalonService>>() {
            @Override
            public void onResponse(Call<List<SalonService>> call, Response<List<SalonService>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this,"Code: " + response.code(),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                salonServiceList = response.body();
                RecyclerView recyclerView = findViewById(R.id.recycler_view_salon);
                RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(MainActivity.this, salonServiceList);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                recyclerView.setAdapter(viewAdapter);
            }

            @Override
            public void onFailure(Call<List<SalonService>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Code: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

//        salonList.add(new Salon("Barber Shop Vũ Trí", "Giảm 30% dịch vụ cắt tóc", "69 Trần Duy Hưng, HN", des1, shop2, "30%", 4.9));
//        salonList.add(new Salon("Tony Barber", "Giảm 10% dịch vụ cắt tóc", "1050 Nguyễn Oanh, HCM", des1, shop3, "10%", 4.8));
//        salonList.add(new Salon("Paris Hair Salon", "Giảm 20% dịch vụ cắt tóc", "123 Gò Vấp, HCM", des1, shop4, "20%", 4.7));
//        salonList.add(new Salon("4RAU Barber Shop", "Giảm 50% dịch vụ cắt tóc", "509 Quang Trung, HCM", des1, shop1, "50%", 4.5));
//        salonList.add(new Salon("Barber Shop Vũ Trí", "Giảm 30% dịch vụ cắt tóc", "69 Trần Duy Hưng, HN", des1, shop2, "30%", 4.9));
//        salonList.add(new Salon("Tony Barber", "Giảm 10% dịch vụ cắt tóc", "1050 Nguyễn Oanh, HCM", des1, shop3, "10%", 4.8));
//        salonList.add(new Salon("Barber Shop Vũ Trí", "Giảm 30% dịch vụ cắt tóc", "69 Trần Duy Hưng, HN", des1, shop2, "30%", 4.9));
//        salonList.add(new Salon("Tony Barber", "Giảm 10% dịch vụ cắt tóc", "1050 Nguyễn Oanh, HCM", des1, shop3, "10%", 4.8));

        // recycler view for recent sale


        // recycler view for newest sale
        RecyclerView recyclerViewNewest = findViewById(R.id.recycler_view_newest);
        RecyclerViewNewestAdapter viewNewestAdapter = new RecyclerViewNewestAdapter(this, salonServiceList);
        recyclerViewNewest.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerViewNewest.setAdapter(viewNewestAdapter);
        // recycler view for best salon
        RecyclerView recyclerViewBestSalon = findViewById(R.id.recycler_view_best_salon);

        recyclerViewBestSalon.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerViewBestSalon.setAdapter(viewNewestAdapter);

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
