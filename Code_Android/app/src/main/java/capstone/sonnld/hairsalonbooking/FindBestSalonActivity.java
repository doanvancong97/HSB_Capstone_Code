package capstone.sonnld.hairsalonbooking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewBestServiceAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSalonByRatingAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelSalon;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;
import capstone.sonnld.hairsalonbooking.support.GeoPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FindBestSalonActivity extends AppCompatActivity {


    // api
    private HairSalonAPI hairSalonAPI;

    //views
    private Button btnSearch;
    private EditText edtSearch;
    private RecyclerView recyclerViewBestSalon;
    private RecyclerViewSalonByRatingAdapter recyclerViewSalonByRatingAdapter;
    private ArrayList<ModelSalon> bestSalons = new ArrayList<>();
    private LatLng you = null;
    private Spinner spDiscount, spRating, spLocation;
    private int discountPriority=1, ratingPriority=1, locationPriority=1;
    private String[] arrSpinner = {"Thấp", "Trung bình","Cao"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_best_salon);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        // init views
        recyclerViewBestSalon = findViewById(R.id.recycler_best_salon);
        btnSearch = findViewById(R.id.btn_search);


        //trong so

        spDiscount= findViewById(R.id.spDiscount);
        spRating= findViewById(R.id.spRating);
        spLocation= findViewById(R.id.spLocation);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(FindBestSalonActivity.this,android.R.layout.simple_spinner_item, arrSpinner);
        spDiscount.setAdapter(adapter);
        spRating.setAdapter(adapter);
        spLocation.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                discountPriority = spDiscount.getSelectedItemPosition()+1;
                ratingPriority = spRating.getSelectedItemPosition()+1;
                locationPriority = spLocation.getSelectedItemPosition()+1;
                bestSalons.clear();
                searchSalon();


            }
        });

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
                    Toast.makeText(FindBestSalonActivity.this, "Không tìm thấy salon nào", Toast.LENGTH_SHORT).show();

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
                    if (ActivityCompat.checkSelfPermission(FindBestSalonActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FindBestSalonActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    int nMaxService = 3;
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
        recyclerViewBestSalon.setLayoutManager(new GridLayoutManager(FindBestSalonActivity.this, 1));
        recyclerViewSalonByRatingAdapter  = new RecyclerViewSalonByRatingAdapter(this, bestSalons);
        recyclerViewBestSalon.setAdapter(recyclerViewSalonByRatingAdapter);
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
