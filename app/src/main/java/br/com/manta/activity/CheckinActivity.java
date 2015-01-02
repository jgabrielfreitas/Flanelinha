package br.com.manta.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;

public class CheckinActivity extends ActionBarActivity implements View.OnClickListener {

    public static LatLng    lastCoordinates; // checkin location
    public static Location  location;        // currentLocation

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private Button    checkinButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        setUpMapIfNeeded();
        instanceViews();
    }

    private void instanceViews() {
        checkinButton = (Button) findViewById(R.id.checkinButton);
        checkinButton.setOnClickListener(this);

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null)
                setUpMap();
        }
    }

    private void setUpMap() {
        googleMap.setMyLocationEnabled(true);
        if (lastCoordinates != null)
            googleMap.addMarker(new MarkerOptions().position(lastCoordinates).title("Seu carro").visible(true));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12));

    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void onClick(View v) {
        if(location != null)
            Utils.createCheckin(location, this);

        Toast.makeText(this, "Localização: " + location.getLatitude() + " , " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }

}
