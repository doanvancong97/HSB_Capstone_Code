package capstone.sonnld.hairsalonbooking;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewServiceByDiscountAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewServiceByRatingAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.Account;
import capstone.sonnld.hairsalonbooking.model.BookingDetail;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import capstone.sonnld.hairsalonbooking.model.SessionManager;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    // api
    private HairSalonAPI hairSalonAPI;

    Button btn_ReLogin;
    LinearLayout lnWelcome;
    TextView txtWelcome;
    NavigationView navigationview;

    // recycler
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewBestService;
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

    // list salon address, name
    private ArrayList<String> addressList = new ArrayList<>();
    private ArrayList<SalonService> salonServiceArrayList = new ArrayList<>();

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

        //end setup sideBar


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


        btn_ReLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                sessionManager = new SessionManager(getApplicationContext());
                if (!sessionManager.isLogin()) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
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
        recyclerViewBestService = findViewById(R.id.recycler_view_good_service);
        recyclerViewBestService.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        getAllSalonServiceByRating();

        // setup search filter
        recyclerViewFilterService = findViewById(R.id.recycler_view_filter_service);
        recyclerViewFilterService.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        getSearchItems();


        // setup search view

        mSearchView = findViewById(R.id.search_view);
        homeLayout = findViewById(R.id.home_layout);
        homeLayout.requestFocus();
        searchResultLayout = findViewById(R.id.show_search_result_layout);


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
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("salonServiceList", salonServiceArrayList);
                    startActivity(intent);

                    return;
                }


                Dexter.withActivity(MainActivity.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("salonServiceList", salonServiceArrayList);
                        startActivity(intent);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Khong dc cap quyen")
                                    .setMessage("Hay cho phep truy cap Location")
                                    .setNegativeButton("cancel", null)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("pakage", getPackageName(), null));

                                        }
                                    }).show();

                        } else
                            Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
            }
        });


    }

    private void initUserDetail(){
        Call<Account> call = hairSalonAPI.getUserDetail(mUserName);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account currentAcc = response.body();
                String avatarUrl = currentAcc.getAvatar();
                String fullName = currentAcc.getFullname();
                Picasso.with(MainActivity.this).load(avatarUrl).into(imgAvatar);
                txtWelcome.setText("Xin chào, " + fullName );
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

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
        hairSalonAPI.getAllServiceByDiscountValue()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<SalonService>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<SalonService> salonServices) {
                        displayFilterService(salonServices);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void displayFilterService(ArrayList<SalonService> services) {
        filterServiceAdapter = new RecyclerViewFilterServiceAdapter(MainActivity.this, services);
        recyclerViewFilterService.setAdapter(filterServiceAdapter);
        searchResultLayout.setVisibility(View.GONE);
    }

    private void getAllSalonServiceByRating() {
        hairSalonAPI.getAllServiceByRating()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<BookingDetail>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<BookingDetail> bookingDetails) {
                        displayServiceByRating(bookingDetails);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void displayServiceByRating(ArrayList<BookingDetail> bookingDetails) {
        RecyclerViewServiceByRatingAdapter viewNewestAdapter = new RecyclerViewServiceByRatingAdapter
                (MainActivity.this, bookingDetails);
        recyclerViewBestService.setAdapter(viewNewestAdapter);
    }

    private void getAllSalonServiceByDiscount() {

        hairSalonAPI.getAllServiceByDiscountValue()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<SalonService>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<SalonService> salonServices) {
                        displayServiceByDiscount(salonServices);
                        getAllAddress(salonServices);
                        salonServiceArrayList = new ArrayList<>(salonServices);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getAllAddress(ArrayList<SalonService> salonServices) {
        String addr;
        for (int i = 0; i < salonServices.size(); i++) {
            addr = salonServices.get(i).getSalon().getAddress().getStreet();
            addressList.add(addr);
        }

    }

    private void displayServiceByDiscount(ArrayList<SalonService> salonServices) {
        RecyclerViewServiceByDiscountAdapter viewAdapter
                = new RecyclerViewServiceByDiscountAdapter(MainActivity.this, salonServices);
        recyclerView.setAdapter(viewAdapter);
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


    public void clickToLogout(MenuItem item) {

        sessionManager.logout();
        finish();
        startActivity(getIntent());

    }

    public void goToUserDetail(View view) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra("username",mUserName);
        startActivity(intent);
    }
}
