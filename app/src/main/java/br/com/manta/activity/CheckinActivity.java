package br.com.manta.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;

public class CheckinActivity extends ActionBarActivity implements View.OnClickListener {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    FloatingActionButton floatAbout;
    FloatingActionButton floatCheckin;
    MarkerOptions markerOptions;
    Marker marker;
    private String  titleMarker = "Você está aqui!";
    LatLng currentPosition;
    public static Location location;
    private LocationManager locationManager;
    ProgressDialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin2);

        gettingInformations();

        setUpMapIfNeeded();
        instanceViews();
    }

    private void instanceViews() {

        floatAbout = (FloatingActionButton) findViewById(R.id.fab_menu_about);
        floatAbout.setIcon(R.drawable.ic_action_info_outline);
        floatAbout.setSize(FloatingActionButton.SIZE_MINI);

        floatCheckin = (FloatingActionButton) findViewById(R.id.fab_menu_checkin);
        floatCheckin.setIcon(R.drawable.ic_maps_pin_drop);
        floatCheckin.setSize(FloatingActionButton.SIZE_MINI);


        floatAbout.setOnClickListener(this);
        floatCheckin.setOnClickListener(this);
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

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        markerOptions = new MarkerOptions();
        markerOptions.position(currentPosition).title(titleMarker).snippet("Rua Juiz de Fora");

        marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();

        zoomInCurrentLocation();

        if(dialog.isShowing())
            dialog.dismiss();

    }

    public void onClick(View view) {

        switch (view.getId()){

            case R.id.fab_menu_about:
                Intent intent = new Intent(this, AboutApplicationActivity.class);
                startActivity(intent);
                break;

            case R.id.fab_menu_checkin:

//                // just vibrate device
//                Utils.vibrateFeedback(getApplicationContext());
//
//                // save cache with position and information about the location
//                if(markerOptions != null) {
//                    String name    = localNameEditText.getText().toString();
//                    String details = detailsLocalEditText.getText().toString();
//                    Utils.createCheckin(name, details, markerOptions.getPosition(), this);
//
//                    Toast.makeText(this, "Seu check-in foi concluído com sucesso!",Toast.LENGTH_LONG).show();
//                    finish();
//                } else
//                    Toast.makeText(this, "Oops.. houve um erro ao realizar o check-in.",Toast.LENGTH_LONG).show();
                break;

        }
    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private synchronized void gettingInformations(){

        dialog = ProgressDialog.show(this, null, "Carregando..", false, false);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        location = Utils.getLocation(locationManager);


    }

    private void zoomInCurrentLocation(){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
    }


}
