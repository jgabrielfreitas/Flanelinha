package br.com.manta.activity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;
import br.com.manta.route.Route;
import br.com.manta.route.Routing;
import br.com.manta.route.RoutingListener;

public class CheckinActivity extends ActionBarActivity implements View.OnClickListener, RoutingListener {

    public static LatLng    lastCoordinates; // checkin location
    public static Location  location;        // currentLocation

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private Button    checkinButton;
    private TextView  noteTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        setUpMapIfNeeded();
        instanceViews();
    }

    private void instanceViews() {
        noteTextView  = (TextView) findViewById(R.id.noteTextView);
        checkinButton = (Button) findViewById(R.id.checkinButton);

        noteTextView.setText(Html.fromHtml(getString(R.string.note)));
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
            googleMap.addMarker(new MarkerOptions().position(lastCoordinates).title("Seu carro").snippet("Último check-in").visible(true));
        else
            Log.e("LOCATION","LOCATION NOT FOUND, LAST COORDINATES IS NULL");

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));

    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        if (lastCoordinates != null && location != null) {
            // create and draw the route
            LatLng start = new LatLng(location.getLatitude(), location.getLongitude());
            Routing routing = new Routing(Routing.TravelMode.WALKING);
            routing.registerListener(this);
            routing.execute(start, lastCoordinates);
        }
    }

    public void onClick(View v) {
        if(location != null)
            Utils.createCheckin(location, this);

        Toast.makeText(this, "Localização: " + location.getLatitude() + " , " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }

    public void onRoutingFailure() {
    }

    public void onRoutingStart() {
    }

    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {
        PolylineOptions polyoptions = new PolylineOptions();
        polyoptions.color(Color.parseColor("#303F9F"));
        polyoptions.width(15);
        polyoptions.addAll(mPolyOptions.getPoints());
        googleMap.addPolyline(polyoptions);
    }
}
