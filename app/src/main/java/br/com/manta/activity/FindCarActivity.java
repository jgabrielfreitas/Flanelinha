package br.com.manta.activity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import br.com.manta.informations.LocationXml;
import br.com.manta.mantaray.R;
import br.com.manta.route.Route;
import br.com.manta.route.Routing;
import br.com.manta.route.RoutingListener;

public class FindCarActivity extends ActionBarActivity implements RoutingListener {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    public static LocationXml cacheLocation; // checkin currentLocation
    public static Location  currentLocation; // currentLocation

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_car);
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        if (cacheLocation != null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(cacheLocation.latitude, cacheLocation.longitude)).title("Seu carro").snippet("Último check-in");

            Marker marker = googleMap.addMarker(markerOptions);
            marker.showInfoWindow();
        }

        if (cacheLocation != null && currentLocation != null) {
            // create and draw the route
            LatLng start = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            LatLng end   = new LatLng(cacheLocation.latitude, cacheLocation.longitude);
            Routing routing = new Routing(Routing.TravelMode.WALKING);
            routing.registerListener(this);
            routing.execute(start, end);
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15));
        googleMap.setMyLocationEnabled(true);
    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }

    public void onRoutingFailure() {}

    public void onRoutingStart() {}

    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {
        PolylineOptions polyoptions = new PolylineOptions();
        polyoptions.color(Color.parseColor("#303F9F"));
        polyoptions.width(15);
        polyoptions.addAll(mPolyOptions.getPoints());
        googleMap.addPolyline(polyoptions);
    }
}
