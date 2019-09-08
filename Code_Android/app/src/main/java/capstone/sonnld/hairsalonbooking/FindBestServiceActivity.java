package capstone.sonnld.hairsalonbooking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewBestServiceAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;
import capstone.sonnld.hairsalonbooking.support.GeoPoint;
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
    private RecyclerViewBestServiceAdapter recyclerViewSalonByRatingAdapter;
    private ArrayList<ModelSalonService> bestServices = new ArrayList<>();
    private LatLng you = null;

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

        recyclerViewBestService.setLayoutManager(new GridLayoutManager(FindBestServiceActivity.this, 1));

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
                if (serviceArrayList == null) {
                    //clear old list
                    bestServices.clear();
                    recyclerViewSalonByRatingAdapter.notifyDataSetChanged();
                    Toast.makeText(FindBestServiceActivity.this, "Không tìm thấy dịch vụ nào", Toast.LENGTH_SHORT).show();

                } else {
                    // sort by discount
                    Collections.sort(serviceArrayList, new Comparator<ModelSalonService>() {
                        @Override
                        public int compare(ModelSalonService o1, ModelSalonService o2) {
                            return Integer.compare(o2.getModelDiscount().getDiscountValue(), o1.getModelDiscount().getDiscountValue());
                        }

                    });
                    // add discount point
                    for (int i = 0; i < serviceArrayList.size(); i++) {
                        if (i > 0) {
                            if (serviceArrayList.get(i).getModelDiscount().getDiscountValue() == serviceArrayList.get(i - 1).getModelDiscount().getDiscountValue()) {
                                serviceArrayList.get(i).setDiscountPoint(serviceArrayList.get(i - 1).getDiscountPoint());
                            }else{
                                serviceArrayList.get(i).setDiscountPoint(serviceArrayList.get(i - 1).getDiscountPoint() - 1);
                            }
                        } else {
                            serviceArrayList.get(i).setDiscountPoint(serviceArrayList.size() - i);
                        }
                    }

                    // sort by rating
                    Collections.sort(serviceArrayList, new Comparator<ModelSalonService>() {
                        @Override
                        public int compare(ModelSalonService o1, ModelSalonService o2) {
                            return Float.compare(o2.getModelSalon().getAverageRating(), o1.getModelSalon().getAverageRating());
                        }

                    });
                    // add rating point
                    for (int i = 0; i < serviceArrayList.size(); i++) {
                        if (i > 0) {
                            if (serviceArrayList.get(i).getModelSalon().getAverageRating() == serviceArrayList.get(i - 1).getModelSalon().getAverageRating()) {
                                serviceArrayList.get(i).setRatingPoint(serviceArrayList.get(i - 1).getRatingPoint());
                            }else{
                                serviceArrayList.get(i).setRatingPoint(serviceArrayList.get(i - 1).getRatingPoint() - 1);
                            }
                        } else {
                            serviceArrayList.get(i).setRatingPoint(serviceArrayList.size() - i);
                        }
                    }
                    // get distance
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(FindBestServiceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FindBestServiceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


                    for (int i = 0; i < serviceArrayList.size(); i++) {
                        String address = serviceArrayList.get(i).getModelSalon().getModelAddress().getStreetNumber() + ", "
                                + serviceArrayList.get(i).getModelSalon().getModelAddress().getStreet() + ", "
                                + serviceArrayList.get(i).getModelSalon().getModelAddress().getModelDistrict().getDistrictName() + ", "
                                + serviceArrayList.get(i).getModelSalon().getModelAddress().getModelDistrict().getModelCity().getCityName();


                        GeoPoint loc = getLocationFromAddress(address);
                        LatLng point = new LatLng(loc.getLat(), loc.getLng());

                        double d = getDistance(you, point);

                        serviceArrayList.get(i).setDistance(d);
                    }
                    // get sort distance
                    Collections.sort(serviceArrayList, new Comparator<ModelSalonService>() {
                        @Override
                        public int compare(ModelSalonService o1, ModelSalonService o2) {
                            return Double.compare(o1.getDistance(), o2.getDistance());
                        }

                    });
                    // add distance point
                    for (int i = 0; i < serviceArrayList.size(); i++) {
                        if (i > 0) {
                            if (serviceArrayList.get(i).getDistance() == serviceArrayList.get(i - 1).getDistance()) {
                                serviceArrayList.get(i).setDistancePoint(serviceArrayList.get(i - 1).getDistancePoint());
                            }else{
                                serviceArrayList.get(i).setDistancePoint(serviceArrayList.get(i - 1).getDistancePoint() - 1);
                            }
                        } else {
                            serviceArrayList.get(i).setDistancePoint(serviceArrayList.size() - i);
                        }

                    }

                    // caculate avgPoint
                    for (int i = 0; i < serviceArrayList.size(); i++) {
                        double avgPoint = (serviceArrayList.get(i).getDiscountPoint() * 0.4
                                + serviceArrayList.get(i).getRatingPoint() * 0.3
                                + serviceArrayList.get(i).getDistancePoint() * 0.3);
                        serviceArrayList.get(i).setAvgPoint(avgPoint);
                    }
                    // sort by avg point
                    Collections.sort(serviceArrayList, new Comparator<ModelSalonService>() {
                        @Override
                        public int compare(ModelSalonService o1, ModelSalonService o2) {
                            return Double.compare(o2.getAvgPoint(), o1.getAvgPoint());
                        }

                    });
                    // add maximun 3 best service
                    int nMaxService = 3;
                    if (nMaxService > serviceArrayList.size()) {
                        nMaxService = serviceArrayList.size();
                    }

                    for (int i = 0; i < nMaxService; i++) {
                        ModelSalonService service = serviceArrayList.get(i);
                        System.out.println(
                                "\n Service name: " + service.getModelService().getServiceName()
                                        + ", Discount is: " + service.getModelDiscount().getDiscountValue()
                                        + ", Discount point: " + service.getDiscountPoint()
                                        + ", Rating is: " + service.getModelSalon().getAverageRating()
                                        + ", Rating point : " + service.getRatingPoint()
                                        + ", Distance is: " + service.getDistance()
                                        + ", distance point : " + service.getDistancePoint()
                                        + ", Avg point: " + service.getAvgPoint() + "\n");
                        bestServices.add(serviceArrayList.get(i));
                    }
                    displayServices(bestServices);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<ModelSalonService>> call, Throwable t) {

            }
        });
    }

    private void displayServices(ArrayList<ModelSalonService> serviceArrayList) {
        recyclerViewSalonByRatingAdapter =
                new RecyclerViewBestServiceAdapter(FindBestServiceActivity.this, serviceArrayList);
        recyclerViewBestService.setAdapter(recyclerViewSalonByRatingAdapter);
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
