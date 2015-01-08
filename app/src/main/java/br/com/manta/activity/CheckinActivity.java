package br.com.manta.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;

public class CheckinActivity extends ActionBarActivity implements View.OnClickListener, GoogleMap.OnMapClickListener,
                                                                  GoogleMap.OnMapLongClickListener {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private Button    checkinButton;
    private TextView  noteTextView;
    EditText localNameEditText;
    EditText detailsLocalEditText;
    MarkerOptions markerOptions;
    Marker marker;

    private String titleMarker = "Você está aqui!";
    private String titleNewMarker = "Local escolhido";

    public static Location location;  // user location

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        setUpMapIfNeeded();
        instanceViews();

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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void instanceViews() {
        noteTextView  = (TextView) findViewById(R.id.noteTextView);
        checkinButton = (Button) findViewById(R.id.checkinButton);

        localNameEditText    = (EditText) findViewById(R.id.localNameEditText);
        detailsLocalEditText = (EditText) findViewById(R.id.detailsLocalEditText);

        noteTextView.setText(Html.fromHtml(getString(R.string.note)));
        checkinButton.setOnClickListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this); // created in beta2
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

        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        //googleMap.setMyLocationEnabled(true);

        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        markerOptions = new MarkerOptions();
        markerOptions.position(currentPosition).title(titleMarker).draggable(true);

        marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));

    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void onClick(View v) {

        // just vibrate device
        Utils.vibrateFeedback(getApplicationContext());

        // save cache with position and information about the location
        if(markerOptions != null) {
            String name    = localNameEditText.getText().toString();
            String details = detailsLocalEditText.getText().toString();
            Utils.createCheckin(name, details, markerOptions.getPosition(), this);

            Toast.makeText(this, "Seu check-in foi concluído com sucesso!",Toast.LENGTH_LONG).show();
            this.finish();
        } else
            Toast.makeText(this, "Oops.. houve um erro ao realizar o check-in.",Toast.LENGTH_LONG).show();
    }

    public void onMapClick(LatLng latLng) {

        /*
        * TODO [event on touch map]
        * create a marker on user touch to indicate current position
        * AND start map with GPS position, not the float blue indicator
        * */

        // Creating a marker
        markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(titleNewMarker);

        // user can drag the marker
        markerOptions.draggable(true);

        // Clears the previously touched position
        googleMap.clear();

        // Animating to the touched position
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();


     }

    public void onMapLongClick(LatLng latLng) {
        // changed in beta2
        DetailsMapActivity.currentLatLng = markerOptions.getPosition();
        Intent intent = new Intent(this, DetailsMapActivity.class);
        startActivity(intent);
        googleMap.clear();
    }

    protected void onStop() {
        super.onStop();

        // to force recreate the map
        googleMap = null;
    }
}
