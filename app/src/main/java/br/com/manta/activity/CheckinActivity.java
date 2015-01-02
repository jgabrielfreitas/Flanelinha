package br.com.manta.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.manta.mantaray.R;

public class CheckinActivity extends ActionBarActivity {

    public static  LatLng    lastCoordinates; // my location
    private        GoogleMap mMap; // Might be null if Google Play services APK is not available.

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
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
        mMap.addMarker(new MarkerOptions().position(lastCoordinates).title("Seu carro"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastCoordinates, 15));
        mMap.setMyLocationEnabled(true);

    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
}
