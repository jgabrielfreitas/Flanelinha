package br.com.manta.activity;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.manta.mantaray.R;

public class DetailsMap extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public static Location currentLocation;
    public static LatLng   currentLatLng;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_map);
        setUpMapIfNeeded();
    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        if (currentLocation != null) {

            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLatLng).title("Você").snippet("Sua localização atual");

            Marker marker = mMap.addMarker(markerOptions);
            marker.showInfoWindow();

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
        }
    }

    protected void onStop() {
        super.onStop();
        // TODO put current currentLocation and chec-in currentLocation = null

    }
}
