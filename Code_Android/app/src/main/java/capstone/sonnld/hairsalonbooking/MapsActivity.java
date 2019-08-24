package capstone.sonnld.hairsalonbooking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anthonyfdev.dropdownview.DropDownView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.ion.Ion;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;
import capstone.sonnld.hairsalonbooking.support.GeoPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private RippleBackground rp_bg;
    private HairSalonAPI hairSalonAPI;
    //Button btn5km;
    //
    private ArrayList<ModelSalonService> modelSalonServices = new ArrayList<>();

    private TextView salon_service_name;
    private LinearLayout lnDeatailOfMarker;
    private TextView salon_address;
    private ImageView salon_img;
    private final int RADIUS = 3000;

    private String address;

    private CardView cardViewSalonDetail;

    private GeoPoint loc = null;
    private LatLng point = null;
    private Spinner spRadius;
    private LatLng you = null;


    private String salonStartTime ;
    private String salonEndTime ;
    private int salonSlotTime ;
    private int salonBookingDay ;
    private int bookingPerSlot = 0;
    private float avgRating = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        rp_bg = findViewById(R.id.rp_bg);
        salon_service_name = findViewById(R.id.salon_service_name);
        lnDeatailOfMarker = findViewById(R.id.lnDeatailOfMarker);
        salon_address = findViewById(R.id.salon_address);
        cardViewSalonDetail = findViewById(R.id.card_view_salon_detail);

        spRadius = findViewById(R.id.spRadius);
        List<String> listRadius = new ArrayList<>();
        listRadius.add("3000 m");
        listRadius.add("4000 m");
        listRadius.add("5000 m");
        listRadius.add("6000 m");

        ArrayAdapter<String> radiusAdapter = new ArrayAdapter<String>(MapsActivity.this, R.layout.spinner_item, listRadius);
        radiusAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spRadius.setAdapter(radiusAdapter);


        //init retro
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);
        salon_img = findViewById(R.id.salon_img);

        // get data from main activity
        Intent intent = getIntent();
        modelSalonServices = (ArrayList<ModelSalonService>) intent.getSerializableExtra("salonServiceList");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mMap = googleMap;
        // MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_maps);


        // mMap.setMapStyle(mapStyleOptions);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        if (location != null) {
            you = new LatLng(location.getLatitude(), location.getLongitude());


            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(you, 13));


            for (int i = 0; i < modelSalonServices.size(); i++) {
                address = modelSalonServices.get(i).getModelSalon().getModelAddress().getStreetNumber() + ", "
                        + modelSalonServices.get(i).getModelSalon().getModelAddress().getStreet() + ", "
                        + modelSalonServices.get(i).getModelSalon().getModelAddress().getModelDistrict().getDistrictName() + ", "
                        + modelSalonServices.get(i).getModelSalon().getModelAddress().getModelDistrict().getModelCity().getCityName();


                String salonName = modelSalonServices.get(i).getModelSalon().getName();
                int salonId = modelSalonServices.get(i).getModelSalon().getSalonId();
                String logoUrl = modelSalonServices.get(i).getModelSalon().getLogoUrl();
                loc = getLocationFromAddress(address);
                point = new LatLng(loc.getLat(), loc.getLng());

                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(point);
                markerOptions.title(salonName);
                markerOptions.snippet(salonId + "");


                int height = 120;
                int width = 120;
//                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.logo_30_shine);
//                Bitmap b = bitmapdraw.getBitmap();
//                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//
//
//
//                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                try {
                    Bitmap bmImg = Ion.with(this)
                            .load(logoUrl).asBitmap().get();

                    Bitmap smallMarker = Bitmap.createScaledBitmap(bmImg, width, height, false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (getDistance(you, point) < RADIUS) {
                    mMap.addMarker(markerOptions).setTag(address);

                }


                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {


                        Toast.makeText(MapsActivity.this, marker.getSnippet(), Toast.LENGTH_SHORT).show();
                        int id = Integer.parseInt(marker.getSnippet());
                        getSalonById(id);
                        salon_service_name.setText(marker.getTitle());
                        salon_address.setText(marker.getTag().toString());
                        lnDeatailOfMarker.setVisibility(View.VISIBLE);

                        cardViewSalonDetail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(MapsActivity.this, DetailSalonActivity.class);
                                intent.putExtra("SalonId", Integer.parseInt(marker.getSnippet()));
                                intent.putExtra("SalonName", marker.getTitle());
                                startActivity(intent);
                                finish();
                            }
                        });

                        return true;

                    }
                });

            }
            mMap.addCircle(new CircleOptions()
                    .center(you)
                    .radius(RADIUS)
                    .strokeColor(Color.GREEN));


            spRadius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String[] arrRadius = parent.getItemAtPosition(position).toString().split(" ");

                    int r = Integer.parseInt(arrRadius[0]);

                    mMap.clear();

                    for (int i = 0; i < modelSalonServices.size(); i++) {
                        address = modelSalonServices.get(i).getModelSalon().getModelAddress().getStreetNumber() + ", "
                                + modelSalonServices.get(i).getModelSalon().getModelAddress().getStreet() + ", "
                                + modelSalonServices.get(i).getModelSalon().getModelAddress().getModelDistrict().getDistrictName() + ", "
                                + modelSalonServices.get(i).getModelSalon().getModelAddress().getModelDistrict().getModelCity().getCityName();
                        String salonName = modelSalonServices.get(i).getModelSalon().getName();
                        int salonId = modelSalonServices.get(i).getModelSalon().getSalonId();
                        String logoUrl = modelSalonServices.get(i).getModelSalon().getLogoUrl();
                        loc = getLocationFromAddress(address);
                        point = new LatLng(loc.getLat(), loc.getLng());

                        MarkerOptions markerOptions = new MarkerOptions();

                        markerOptions.position(point);
                        markerOptions.title(salonName);
                        markerOptions.snippet(salonId + "");

                        int height = 120;
                        int width = 120;

                        try {
                            Bitmap bmImg = Ion.with(MapsActivity.this)
                                    .load(logoUrl).asBitmap().get();

                            Bitmap smallMarker = Bitmap.createScaledBitmap(bmImg, width, height, false);
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (getDistance(you, point) < r) {
                            mMap.addMarker(markerOptions).setTag(address);

                        }


                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(final Marker marker) {


                                Toast.makeText(MapsActivity.this, marker.getSnippet(), Toast.LENGTH_SHORT).show();
                                int id = Integer.parseInt(marker.getSnippet());

                                getSalonById(id);

                                salon_service_name.setText(marker.getTitle());
                                salon_address.setText(marker.getTag().toString());
                                lnDeatailOfMarker.setVisibility(View.VISIBLE);

                                cardViewSalonDetail.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent intent = new Intent(MapsActivity.this, DetailSalonActivity.class);
                                        intent.putExtra("SalonId", Integer.parseInt(marker.getSnippet()));
                                        intent.putExtra("SalonName", marker.getTitle());
                                        intent.putExtra("SalonStartTime",salonStartTime);
                                        intent.putExtra("SalonEndTime",salonEndTime);
                                        intent.putExtra("SalonSlotTime",salonSlotTime);
                                        intent.putExtra("SalonBookingDay",salonBookingDay);
                                        intent.putExtra("SalonBookingPerSlot",bookingPerSlot);
                                        intent.putExtra("AvgRating",avgRating);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                return true;

                            }
                        });


                    }

                    mMap.addCircle(new CircleOptions()
                            .center(you)
                            .radius(r)
                            .strokeColor(Color.GREEN));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }


    private void getSalonById(int salonId) {
        Call<ArrayList<ModelSalonService>> call = hairSalonAPI.getSalonServiceBySalonId(salonId);

        call.enqueue(new Callback<ArrayList<ModelSalonService>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelSalonService>> call, Response<ArrayList<ModelSalonService>> response) {
                ArrayList<ModelSalonService> modelSalonServices = response.body();
                String imgUrl = modelSalonServices.get(0).getModelSalon().getUrl();
                Picasso.with(MapsActivity.this).load(imgUrl).into(salon_img);
                salonStartTime = modelSalonServices.get(0).getModelSalon().getOpenTime();
                salonEndTime = modelSalonServices.get(0).getModelSalon().getCloseTime();
                salonSlotTime =  modelSalonServices.get(0).getModelSalon().getSlotTime();
                salonBookingDay =  modelSalonServices.get(0).getModelSalon().getBookingDay();
                bookingPerSlot =  modelSalonServices.get(0).getModelSalon().getBookingPerSlot();
                avgRating =  modelSalonServices.get(0).getModelSalon().getAverageRating();
            }

            @Override
            public void onFailure(Call<ArrayList<ModelSalonService>> call, Throwable t) {

            }
        });
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
