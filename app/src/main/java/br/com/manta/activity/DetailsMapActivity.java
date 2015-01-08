package br.com.manta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.manta.mantaray.R;

public class DetailsMapActivity extends ActionBarActivity implements GoogleMap.OnMapClickListener{

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.

    public static LatLng currentLatLng;
    MarkerOptions markerOptions;
    Marker marker;

    private String titleMarker = "Você está aqui!";
    private String titleNewMarker = "Local escolhido";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_map);
        setUpMapIfNeeded();

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                                    // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }

                refreshCurrentLocation();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
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

        if (currentLatLng != null) {

            markerOptions = new MarkerOptions();
            markerOptions.position(currentLatLng).title(markerOptions.getTitle());

            Marker marker = googleMap.addMarker(markerOptions);
            marker.showInfoWindow();

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
        }

        googleMap.setOnMapClickListener(this);
    }

    private void refreshCurrentLocation(){
        // on back activity, return the location from this map
        CheckinActivity.location.setLatitude(markerOptions.getPosition().latitude);
        CheckinActivity.location.setLongitude(markerOptions.getPosition().longitude);
    }


    public void onMapClick(LatLng latLng) {
//        // Creating a marker
        markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(titleNewMarker);

        markerOptions.draggable(true);

        // Clears the previously touched position
        googleMap.clear();

        // Animating to the touched position
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();
    }

    public void onBackPressed() {
        refreshCurrentLocation();
        finish();
    }
}
