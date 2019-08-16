package capstone.sonnld.hairsalonbooking;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewFilterServiceAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSalonByRatingAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewServiceByDiscountAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelAccount;
import capstone.sonnld.hairsalonbooking.model.ModelSalon;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;
import capstone.sonnld.hairsalonbooking.support.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    // api
    private HairSalonAPI hairSalonAPI;

    private Button btn_ReLogin;
    private LinearLayout lnWelcome;
    private TextView txtWelcome;
    private NavigationView navigationview;
    MenuItem menuHistory;

    // recycler
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewSalonByRating;
    private RecyclerView recyclerViewFilterService;

    // menu and toolbar
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;

    // search
    private TextView txtService;
    private SearchView mSearchView;


    // adapter
    private RecyclerViewFilterServiceAdapter filterServiceAdapter;

    // layout
    private LinearLayout homeLayout;
    private LinearLayout searchResultLayout;
    private int userID;

    // list salon address, name

    private ArrayList<ModelSalonService> modelSalonServiceArrayList = new ArrayList<>();

    // btn location
    private ImageView btnLocation;

    //Session Login
    private SessionManager sessionManager;
    private String mUserName;
    private ImageView imgAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        //setup tool bar
        mToolbar = findViewById(R.id.nav_action_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //setup menu sideBar
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();


        // User detail setup
        navigationview = findViewById(R.id.navigationview);
        View header = navigationview.getHeaderView(0);

        btn_ReLogin = header.findViewById(R.id.btn_ReLogin);
        lnWelcome = header.findViewById(R.id.lnWelcome);
        txtWelcome = header.findViewById(R.id.txtWelcome);
        imgAvatar = header.findViewById(R.id.img_avatar);

        lnWelcome.setVisibility(View.GONE);


        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLogin()) {

            HashMap<String, String> user = sessionManager.getUserDetail();
            mUserName = user.get(sessionManager.getUSERNAME());

            lnWelcome.setVisibility(View.VISIBLE);
            initUserDetail();
            btn_ReLogin.setVisibility(View.GONE);



        }

        //onclick login
        btn_ReLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionManager = new SessionManager(getApplicationContext());
                if (!sessionManager.isLogin()) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                HashMap<String, String> user = sessionManager.getUserDetail();
                mUserName = user.get(sessionManager.getUSERNAME());
                initUserDetail();

            }
        });


        //recycler view for service by discount
        recyclerView = findViewById(R.id.recycler_view_salon);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        getAllSalonServiceByDiscount();

        // recycler view for service by rating
        recyclerViewSalonByRating = findViewById(R.id.recycler_view_salon_by_rating);
        recyclerViewSalonByRating.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
        getAllSalonByRating();

        // setup search filter
        recyclerViewFilterService = findViewById(R.id.recycler_view_filter_service);
        recyclerViewFilterService.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        getSearchItems();


        // setup search view 1

        mSearchView = findViewById(R.id.search_view);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setFocusable(true);
        mSearchView.setIconified(false);
        mSearchView.requestFocusFromTouch();


        homeLayout = findViewById(R.id.home_layout);
        homeLayout.requestFocus();
        searchResultLayout = findViewById(R.id.show_search_result_layout);

        // setup search view 2
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                homeLayout.setVisibility(View.GONE);
                searchResultLayout.setVisibility(View.VISIBLE);
                filterServiceAdapter.getFilter().filter(removeAccent(newText));
                return false;
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchResultLayout.setVisibility(View.GONE);
                homeLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });


        // setup location
        btnLocation = findViewById(R.id.btn_location);

        // location onclick
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager lm = (LocationManager) getApplicationContext().getSystemService(getApplicationContext().LOCATION_SERVICE);
                boolean gps_enabled = false;


                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);


                if (!gps_enabled) {


                    Toast.makeText(MainActivity.this, "Location is off", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Định vị đang tắt")
                            .setMessage("Bạn có muốn bật định vị?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                                }
                            })
                            .setNegativeButton("Không", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {

                    Toast.makeText(MainActivity.this, "Location is on", Toast.LENGTH_SHORT).show();

                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("salonServiceList", modelSalonServiceArrayList);
                        Toast.makeText(MainActivity.this, modelSalonServiceArrayList.size() + "", Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    } else {
                        Dexter.withActivity(MainActivity.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                                intent.putExtra("salonServiceList", modelSalonServiceArrayList);
                                Toast.makeText(MainActivity.this, modelSalonServiceArrayList.size() + "", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                            }


                        }).check();
                    }
//
                }
            }
        });


    }


    // function when reload page
    @Override
    protected void onRestart() {
        super.onRestart();
        if (sessionManager.isLogin()) {

            HashMap<String, String> user = sessionManager.getUserDetail();
            mUserName = user.get(sessionManager.getUSERNAME());

            lnWelcome.setVisibility(View.VISIBLE);
            initUserDetail();
            btn_ReLogin.setVisibility(View.GONE);


        }else {
            btn_ReLogin.setVisibility(View.VISIBLE);
            lnWelcome.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("editTextValue");
            }
        }
    }

    public void goToUserDetail(View view) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra("username", mUserName);
        startActivity(intent);

    }

    private void initUserDetail() {
        Call<ModelAccount> call = hairSalonAPI.getUserDetail(mUserName);
        call.enqueue(new Callback<ModelAccount>() {
            @Override
            public void onResponse(Call<ModelAccount> call, Response<ModelAccount> response) {
                ModelAccount currentAcc = response.body();
                String avatarUrl = currentAcc.getAvatar();
                String fullName = currentAcc.getFullname();
                userID = currentAcc.getUserId();
                Picasso.with(MainActivity.this).load(avatarUrl).into(imgAvatar);
                txtWelcome.setText("Xin chào, " + fullName);
            }

            @Override
            public void onFailure(Call<ModelAccount> call, Throwable t) {

            }
        });
    }

    public String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp)
                .replaceAll("")
                .replaceAll("Đ", "D").replace("đ", "d");
    }


    private void getSearchItems() {
        Call call = hairSalonAPI.getAllServiceByDiscountValue();

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ArrayList<ModelSalonService> modelSalonServices = (ArrayList<ModelSalonService>) response.body();
                displayFilterService(modelSalonServices);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void displayFilterService(ArrayList<ModelSalonService> services) {
        filterServiceAdapter = new RecyclerViewFilterServiceAdapter(MainActivity.this, services);
        recyclerViewFilterService.setAdapter(filterServiceAdapter);
        searchResultLayout.setVisibility(View.GONE);
    }

    // api call for list service by discount
    private void getAllSalonServiceByDiscount() {
        Call call = hairSalonAPI.getAllServiceByDiscountValue();

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ArrayList<ModelSalonService> modelSalonServices = (ArrayList<ModelSalonService>) response.body();
                modelSalonServiceArrayList = new ArrayList<>(modelSalonServices);
                displayServiceByDiscount(modelSalonServices);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void displayServiceByDiscount(ArrayList<ModelSalonService> modelSalonServices) {
        RecyclerViewServiceByDiscountAdapter viewAdapter
                = new RecyclerViewServiceByDiscountAdapter(MainActivity.this, modelSalonServices);
        recyclerView.setAdapter(viewAdapter);
    }

    // api call for list salon by rating
    private void getAllSalonByRating() {
        Call<ArrayList<ModelSalon>> listCall = hairSalonAPI.getAllSalonByRating();

        listCall.enqueue(new Callback<ArrayList<ModelSalon>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelSalon>> call, Response<ArrayList<ModelSalon>> response) {
                ArrayList<ModelSalon> modelSalons = response.body();
                displaySalonByRating(modelSalons);
            }

            @Override
            public void onFailure(Call<ArrayList<ModelSalon>> call, Throwable t) {

            }
        });
    }

    private void displaySalonByRating(ArrayList<ModelSalon> modelSalons) {
        RecyclerViewSalonByRatingAdapter recyclerViewSalonByRatingAdapter =
                new RecyclerViewSalonByRatingAdapter(MainActivity.this,modelSalons);
        recyclerViewSalonByRating.setAdapter(recyclerViewSalonByRatingAdapter);
    }

    public void clickToRedirectToLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void clickToViewHistory(MenuItem item) {

        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        intent.putExtra("USER_ID",userID);
        Toast.makeText(this, userID+"", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
