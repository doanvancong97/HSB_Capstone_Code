package capstone.sonnld.hairsalonbooking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
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
import capstone.sonnld.hairsalonbooking.model.GeoPoint;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private RippleBackground rp_bg;
    private HairSalonAPI hairSalonAPI;
    //
    private ArrayList<SalonService> salonServices = new ArrayList<>();

    private TextView salon_service_name;
    private LinearLayout lnDeatailOfMarker;
    private TextView salon_address;
    private ImageView salon_img;

    private String address;

    private CardView cardViewSalonDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        rp_bg = findViewById(R.id.rp_bg);
        salon_service_name = findViewById(R.id.salon_service_name);
        lnDeatailOfMarker = findViewById(R.id.lnDeatailOfMarker);
        salon_address = findViewById(R.id.salon_address);
        cardViewSalonDetail = findViewById(R.id.card_view_salon_detail);


        //init retro
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);
        salon_img = findViewById(R.id.salon_img);

        // get data from main activity
        Intent intent = getIntent();
        salonServices = (ArrayList<SalonService>) intent.getSerializableExtra("salonServiceList");

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

        GeoPoint loc = null;
        LatLng point = null;

        LatLng you = null;

        if (location != null) {
            you = new LatLng(location.getLatitude(), location.getLongitude());


            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(you, 13));


            for (int i = 0; i < salonServices.size(); i++) {
                address = salonServices.get(i).getSalon().getAddress().getStreetNumber() + ", "
                        + salonServices.get(i).getSalon().getAddress().getStreet();
                String salonName = salonServices.get(i).getSalon().getName();
                int salonId = salonServices.get(i).getSalon().getSalonId();
                String logoUrl = salonServices.get(i).getSalon().getLogoUrl();
                loc = getLocationFromAddress(address);
                point = new LatLng(loc.getLat(), loc.getLng());

                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(point);
                markerOptions.title(salonName);
                markerOptions.snippet(salonId + "");


                int height = 100;
                int width = 100;
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

                mMap.addMarker(markerOptions).setTag(address);


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

                                Toast.makeText(MapsActivity.this, "Đang chuyển tới trang chủ của " + marker.getTitle() + "...", Toast.LENGTH_SHORT).show();
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


//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    rp_bg.startRippleAnimation();
//
//                }
//            }, 1000);
//
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    rp_bg.stopRippleAnimation();
//
//                }
//            }, 2500);


        }

//        mMap.addCircle(new CircleOptions()
//                .center(you)
//                .radius(5000)
//                .strokeColor(Color.RED));


    }
    

    private void getSalonById(int salonId) {
        Call<ArrayList<SalonService>> call = hairSalonAPI.getSalonServiceBySalonId(salonId);

        call.enqueue(new Callback<ArrayList<SalonService>>() {
            @Override
            public void onResponse(Call<ArrayList<SalonService>> call, Response<ArrayList<SalonService>> response) {
                ArrayList<SalonService> salonServices = response.body();
                String imgUrl = salonServices.get(0).getSalon().getUrl();
                Picasso.with(MapsActivity.this).load(imgUrl).into(salon_img);
            }

            @Override
            public void onFailure(Call<ArrayList<SalonService>> call, Throwable t) {

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
}
