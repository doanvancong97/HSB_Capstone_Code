package capstone.sonnld.hairsalonbooking;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewFilterServiceAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSalonByRatingAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewServiceByDiscountAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelAccount;
import capstone.sonnld.hairsalonbooking.model.ModelSalon;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;
import capstone.sonnld.hairsalonbooking.support.GeoPoint;
import capstone.sonnld.hairsalonbooking.support.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    // api
    private HairSalonAPI hairSalonAPI;

    private Button btn_ReLogin;
    private ImageView btnNotify;
    private LinearLayout lnWelcome;
    private TextView txtWelcome;
    private NavigationView navigationview;

    // recycler
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewSalonByRating;
    private RecyclerView recyclerViewFilterService;

    // menu and toolbar
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private Toolbar mToolbar;

    // search
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


    // notify
    private TextView txtNumberNotify;

    // find best salon

    private RecyclerView recyclerViewBestSalon;
    private RecyclerViewSalonByRatingAdapter recyclerViewSalonByRatingAdapter;
    private ArrayList<ModelSalon> bestSalons = new ArrayList<>();
    private LatLng you = null;
    private int discountPriority=1, ratingPriority=1, locationPriority=1;

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
            userID = Integer.parseInt(user.get("userId"));
            //create topic with user id
            FirebaseMessaging.getInstance().subscribeToTopic("User" + userID);
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
                userID = Integer.parseInt(user.get("userId"));
                //create topic with user id
                FirebaseMessaging.getInstance().subscribeToTopic("User" + userID);
                initUserDetail();

            }
        });

        // show best salon
        recyclerViewBestSalon = findViewById(R.id.recycler_view_best_salon);
        searchSalon();


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

        // setup notify
        btnNotify = findViewById(R.id.btn_notify);
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                intent.putExtra("cusId", userID);
                startActivity(intent);
            }
        });
        // count un open notify
        txtNumberNotify = findViewById(R.id.txt_number_notify);

        countUnOpenNotify();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            int state = extras.getInt("extra");
            System.out.println("Extra hereeee: " + state);
            updateView(String.valueOf(state));// update your textView in the main layout
        }
    }

    private void updateView(String state) {
        txtNumberNotify.setVisibility(View.VISIBLE);
        txtNumberNotify.setText(state);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("capstone.sonnld.hairsalonbooking.onMessageReceived");
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        registerReceiver(receiver, intentFilter);
    }

    private void searchSalon() {
        final double totalPriority= discountPriority+ratingPriority+locationPriority;

        Call<ArrayList<ModelSalon>> listCall = hairSalonAPI.getAllSalonByRating();

        listCall.enqueue(new Callback<ArrayList<ModelSalon>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelSalon>> call, Response<ArrayList<ModelSalon>> response) {
                ArrayList<ModelSalon> modelSalons = response.body();

                if (modelSalons == null) {
                    //clear old list
                    bestSalons.clear();
                    recyclerViewSalonByRatingAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Không tìm thấy salon nào", Toast.LENGTH_SHORT).show();

                } else {
                    // sort by discount
                    Collections.sort(modelSalons, new Comparator<ModelSalon>() {
                        @Override
                        public int compare(ModelSalon o1, ModelSalon o2) {
                            return Float.compare(o2.getAvgDiscount(), o1.getAvgDiscount());
                        }

                    });
                    // add discount point
                    for (int i = 0; i < modelSalons.size(); i++) {
                        if (i > 0) {
                            if (modelSalons.get(i).getAvgDiscount() == modelSalons.get(i - 1).getAvgDiscount()) {
                                modelSalons.get(i).setDiscountPoint(modelSalons.get(i - 1).getDiscountPoint());
                            }else{
                                modelSalons.get(i).setDiscountPoint(modelSalons.get(i - 1).getDiscountPoint() - 1);
                            }
                        } else {
                            modelSalons.get(i).setDiscountPoint(modelSalons.size() - i);
                        }
                    }
                    // sort by rating
                    Collections.sort(modelSalons, new Comparator<ModelSalon>() {
                        @Override
                        public int compare(ModelSalon o1, ModelSalon o2) {
                            return Float.compare(o2.getAverageRating(), o1.getAverageRating());
                        }

                    });
                    // add rating point
                    for (int i = 0; i < modelSalons.size(); i++) {
                        if (i > 0) {
                            if (modelSalons.get(i).getAverageRating() == modelSalons.get(i - 1).getAverageRating()) {
                                modelSalons.get(i).setRatingPoint(modelSalons.get(i - 1).getRatingPoint());
                            }else{
                                modelSalons.get(i).setRatingPoint(modelSalons.get(i - 1).getRatingPoint() - 1);
                            }
                        } else {
                            modelSalons.get(i).setRatingPoint(modelSalons.size() - i);
                        }
                    }
                    // get distance
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (location != null) {
                        you = new LatLng(location.getLatitude(), location.getLongitude());
                    }


                    for (int i = 0; i < modelSalons.size(); i++) {
                        String address = modelSalons.get(i).getModelAddress().getStreetNumber() + ", "
                                + modelSalons.get(i).getModelAddress().getStreet() + ", "
                                + modelSalons.get(i).getModelAddress().getModelDistrict().getDistrictName() + ", "
                                + modelSalons.get(i).getModelAddress().getModelDistrict().getModelCity().getCityName();


                        GeoPoint loc = getLocationFromAddress(address);
                        LatLng point = new LatLng(loc.getLat(), loc.getLng());

                        double d = getDistance(you, point);

                        modelSalons.get(i).setDistance(d);
                    }
                    // get sort distance
                    Collections.sort(modelSalons, new Comparator<ModelSalon>() {
                        @Override
                        public int compare(ModelSalon o1, ModelSalon o2) {
                            return Double.compare(o1.getDistance(), o2.getDistance());
                        }

                    });
                    // add distance point
                    for (int i = 0; i < modelSalons.size(); i++) {
                        if (i > 0) {
                            if (modelSalons.get(i).getDistance() == modelSalons.get(i - 1).getDistance()) {
                                modelSalons.get(i).setDistancePoint(modelSalons.get(i - 1).getDistancePoint());
                            }else{
                                modelSalons.get(i).setDistancePoint(modelSalons.get(i - 1).getDistancePoint() - 1);
                            }
                        } else {
                            modelSalons.get(i).setDistancePoint(modelSalons.size() - i);
                        }

                    }

                    // caculate avgPoint
                    for (int i = 0; i < modelSalons.size(); i++) {
                        double avgPoint = (modelSalons.get(i).getDiscountPoint() * (discountPriority/totalPriority)
                                + modelSalons.get(i).getRatingPoint() * (ratingPriority/totalPriority)
                                + modelSalons.get(i).getDistancePoint() * (locationPriority/totalPriority));
                        modelSalons.get(i).setAvgPoint(avgPoint);
                    }
                    // sort by avg point
                    Collections.sort(modelSalons, new Comparator<ModelSalon>() {
                        @Override
                        public int compare(ModelSalon o1, ModelSalon o2) {
                            return Double.compare(o2.getAvgPoint(), o1.getAvgPoint());
                        }

                    });
                    // add maximun 3 best service
                    int nMaxService = 1;
                    if (nMaxService > modelSalons.size()) {
                        nMaxService = modelSalons.size();
                    }
                    bestSalons.clear();
                    for (int i = 0; i < nMaxService; i++) {
                        ModelSalon salon = modelSalons.get(i);
                        System.out.println(
                                "\n Service name: " + salon.getName()
                                        + ", Discount is: " + salon.getAvgDiscount()
                                        + ", Discount point: " + salon.getDiscountPoint()
                                        + ", Rating is: " + salon.getAverageRating()
                                        + ", Rating point : " + salon.getRatingPoint()
                                        + ", Distance is: " + salon.getDistance()
                                        + ", distance point : " + salon.getDistancePoint()
                                        + ", Avg point: " + salon.getAvgPoint() + "\n");
                        bestSalons.add(modelSalons.get(i));
                    }
                    displaySalons(bestSalons);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ModelSalon>> call, Throwable t) {

            }
        });
    }

    private void displaySalons(ArrayList<ModelSalon> bestSalons) {
        recyclerViewBestSalon.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        recyclerViewSalonByRatingAdapter  = new RecyclerViewSalonByRatingAdapter(this, bestSalons);
        recyclerViewBestSalon.setAdapter(recyclerViewSalonByRatingAdapter);
    }

    private void countUnOpenNotify() {
        Call<Integer> integerCall = hairSalonAPI.countUnOpenNotify(userID);
        integerCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int numberOfNotify = response.body();
                System.out.println(numberOfNotify+ "hereeeee");
                txtNumberNotify.setText(String.valueOf(numberOfNotify));
                if(txtNumberNotify.getText().equals("0")){
                    txtNumberNotify.setVisibility(View.GONE);
                }
                if(!txtNumberNotify.getText().equals("0")){
                    txtNumberNotify.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

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
            userID = Integer.parseInt(user.get("userId"));
            lnWelcome.setVisibility(View.VISIBLE);
            initUserDetail();
            btn_ReLogin.setVisibility(View.GONE);
            countUnOpenNotify();

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

                //create topic with user id
                FirebaseMessaging.getInstance().subscribeToTopic("User" + userID);
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
        //Toast.makeText(this, userID+"", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    public void findBestSalon(MenuItem item){
        Intent intent = new Intent(MainActivity.this, FindBestSalonActivity.class);
        startActivity(intent);
    }

    public void findBestService(MenuItem item){
        Intent intent = new Intent(MainActivity.this, FindBestServiceActivity.class);
        startActivity(intent);
    }

    public GeoPoint getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new GeoPoint(location.getLatitude(), location.getLongitude());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }

    public double getDistance(LatLng LatLng1, LatLng LatLng2) {
        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(LatLng1.latitude);
        locationA.setLongitude(LatLng1.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(LatLng2.latitude);
        locationB.setLongitude(LatLng2.longitude);
        distance = locationA.distanceTo(locationB);

        return distance;
    }
}
