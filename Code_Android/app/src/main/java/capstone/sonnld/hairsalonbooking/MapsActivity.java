package capstone.sonnld.hairsalonbooking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import capstone.sonnld.hairsalonbooking.model.GeoPoint;

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
        if (location != null) {
            LatLng you = new LatLng(location.getLatitude(),location.getLongitude());

            GeoPoint loc1 = getLocationFromAddress("65 trần thị hè");
            GeoPoint loc2 = getLocationFromAddress("103 tô ký");
            GeoPoint loc3= getLocationFromAddress("đại học fpt, quận 12");





            LatLng point1 = new LatLng(loc1.getLat(),loc1.getLng());
            LatLng point2 = new LatLng(loc2.getLat(),loc2.getLng());
            LatLng point3 = new LatLng(loc3.getLat(),loc3.getLng());

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(you, 15));
            MarkerOptions markerOptions1 = new MarkerOptions();
            MarkerOptions markerOptions2= new MarkerOptions();

            MarkerOptions markerOptions3 = new MarkerOptions();

            markerOptions1.position(point1);
            markerOptions2.position(point2);
            markerOptions3.position(point3);
            mMap.addMarker(markerOptions1);
            mMap.addMarker(markerOptions2);
            mMap.addMarker(markerOptions3);

            rp_bg.startRippleAnimation();
        }

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
