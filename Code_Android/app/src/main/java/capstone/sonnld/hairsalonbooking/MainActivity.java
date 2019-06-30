package capstone.sonnld.hairsalonbooking;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewFilterServiceAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSalonByDiscountAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSalonByRatingAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.BookingDetail;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    // api
    private HairSalonAPI hairSalonAPI;

    // recycler
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewBestService;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


        //init retro
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        //recycler view for service by discount
        recyclerView = findViewById(R.id.recycler_view_salon);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        getAllSalonServiceByDiscount();

        // recycler view for service by rating
        recyclerViewBestService = findViewById(R.id.recycler_view_good_service);
        recyclerViewBestService.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        getAllSalonServiceByRating();

        // setup filter
        recyclerViewFilterService = findViewById(R.id.recycler_view_filter_service);
        recyclerViewFilterService.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));

        getSearchItems();

        mSearchView = findViewById(R.id.search_view);
        homeLayout = findViewById(R.id.home_layout);
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
        RecyclerViewSalonByRatingAdapter viewNewestAdapter = new RecyclerViewSalonByRatingAdapter
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
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void displayServiceByDiscount(ArrayList<SalonService> salonServices) {
        RecyclerViewSalonByDiscountAdapter viewAdapter
                = new RecyclerViewSalonByDiscountAdapter(MainActivity.this, salonServices);
        recyclerView.setAdapter(viewAdapter);
    }


    public void clickToRedirectToSearch(View view) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
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

    public void clickToRedirectToSearchByLocation(View view) {
//        LocationManager locationManager;
//        String lat, lng;
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        if(location!=null){
//            lat = location.getLatitude()+"";
//            lng= location.getLongitude()+"";
//
//        }
//
        Toast.makeText(this, "location", Toast.LENGTH_SHORT).show();
//
//
//

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(MainActivity.this, MapsActivity.class));

            finish();
            return;
        }


        Dexter.withActivity(MainActivity.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                finish();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                if(response.isPermanentlyDenied()){
                    AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Khong dc cap quyen")
                            .setMessage("Hay cho phep truy cap Location")
                            .setNegativeButton("cancel",null)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.fromParts("pakage", getPackageName(),null));

                                }
                            }).show();

                }

                else
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();





    }
}
