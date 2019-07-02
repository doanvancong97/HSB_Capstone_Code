package capstone.sonnld.hairsalonbooking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import capstone.sonnld.hairsalonbooking.model.GeoPoint;
import capstone.sonnld.hairsalonbooking.model.Salon;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient ;
    PlacesClient placesClient;
    Location mLastKnownLocation;
    LocationCallback locationCallback;
    View mapView;
    final float DEFAULT_ZOOM = 15;
    RippleBackground rp_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        rp_bg = findViewById(R.id.rp_bg);


        SupportMapFragment  mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        List<String> listAddress = new ArrayList<>();
        listAddress.add("21 trần thị hè");
        listAddress.add("103 tô ký");
        listAddress.add("1024 Quang Trung, Gò Vấp ");
        listAddress.add("đại học fpt, quận 12");
        GeoPoint loc = null;
        LatLng point = null;

        LatLng you = null;

        if (location != null) {
            you = new LatLng(location.getLatitude(),location.getLongitude());

//            GeoPoint loc1 = getLocationFromAddress("65 trần thị hè");
//            GeoPoint loc2 = getLocationFromAddress("103 tô ký");
//            GeoPoint loc3= getLocationFromAddress("đại học fpt, quận 12");
//
//
//
//
//
//            LatLng point1 = new LatLng(loc1.getLat(),loc1.getLng());
//            LatLng point2 = new LatLng(loc2.getLat(),loc2.getLng());
//            LatLng point3 = new LatLng(loc3.getLat(),loc3.getLng());
//
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(you, 15));
//            MarkerOptions markerOptions1 = new MarkerOptions();
//            MarkerOptions markerOptions2= new MarkerOptions();
//
//            MarkerOptions markerOptions3 = new MarkerOptions();
//
//            markerOptions1.position(point1);
//            markerOptions2.position(point2);
//            markerOptions3.position(point3);
//            mMap.addMarker(markerOptions1);
//            mMap.addMarker(markerOptions2);
//            mMap.addMarker(markerOptions3);


            for (int i = 0; i <listAddress.size() ; i++) {

                loc = getLocationFromAddress(listAddress.get(i));
                point = new LatLng(loc.getLat(),loc.getLng());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(you,13));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(point);
                markerOptions.snippet(listAddress.get(i));
                markerOptions.title("30 Shine");

                int height = 150;
                int width = 150;
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.logo_30_shine);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));


                mMap.addMarker(markerOptions);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Toast.makeText(MapsActivity.this, "Đang chuyển tới trang chủ của "+marker.getTitle()+"...", Toast.LENGTH_SHORT).show();

                        return false;
                    }
                });



            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rp_bg.startRippleAnimation();

                }
            },2000);




            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rp_bg.stopRippleAnimation();

                }
            },4000);


        }

        mMap.addCircle(new CircleOptions()
                .center(you)
                .radius(5000)
                .strokeColor(Color.RED));


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
