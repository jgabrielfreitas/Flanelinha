package br.com.manta.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.manta.informations.LocationAddress;
import br.com.manta.informations.LocationXml;
import br.com.manta.informations.UserCurrentPlace;
import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;

public class CheckinActivity extends ActionBarActivity implements View.OnClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    FloatingActionButton floatAbout;
    FloatingActionButton floatCheckin;
    FloatingActionButton floatFindCar;
    TextView streetTextView;
    TextView stateTextView;
    MarkerOptions markerOptions;
    Marker marker;
    Location location;
    private String titleMarker = "Você está aqui!";
    private String titleNewMarker = "Local escolhido";
    private LocationManager locationManager;
    LocationXml locationXml = new LocationXml();

    public static UserCurrentPlace userPlace;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin2);

        gettingInformations();

        setUpMapIfNeeded();
        instanceViews();
    }

    private void instanceViews() {

        streetTextView = (TextView) findViewById(R.id.streetTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);

        floatAbout = (FloatingActionButton) findViewById(R.id.fab_about);
        floatAbout.setIcon(R.drawable.ic_action_info_outline);
        floatAbout.setSize(FloatingActionButton.SIZE_MINI);

        floatCheckin = (FloatingActionButton) findViewById(R.id.fab_menu_checkin);
        floatCheckin.setIcon(R.drawable.ic_maps_pin_drop);
        floatCheckin.setSize(FloatingActionButton.SIZE_MINI);

        floatFindCar = (FloatingActionButton) findViewById(R.id.fab_menu_find_car);
        floatFindCar.setIcon(R.drawable.ic_action_explore);
        floatFindCar.setSize(FloatingActionButton.SIZE_MINI);


        floatAbout.setOnClickListener(this);
        floatCheckin.setOnClickListener(this);
        floatFindCar.setOnClickListener(this);
        googleMap.setOnMapClickListener(this);

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
//            if (googleMap != null)
            setUpMap();
        }
    }

    private void setUpMap() {

        markerOptions = new MarkerOptions();
        markerOptions.position(userPlace.getPlaceList().get(0).getLatLng()).title(titleMarker);

        marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();

        zoomInCurrentLocation();

        setAddress(Utils.getLocation(locationManager));
    }

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.fab_about:
                closeFloatMenu();
                Intent intent_about = new Intent(this, AboutApplicationActivity.class);
                startActivity(intent_about);
                break;

            case R.id.fab_menu_find_car:

                if (Utils.justCheckFileCache(Utils.CACHE_LAST_CHECKIN)) {
                    Intent intent_find = new Intent(this, FindCarActivity.class);
                    startActivity(intent_find);
                } else
                    Toast.makeText(this, "Nenhuma localização anterior foi encontrada.\nVocê já realizou o check-in?", Toast.LENGTH_LONG).show();

                closeFloatMenu();

                break;

            case R.id.fab_menu_checkin:

                // just vibrate device
                Utils.vibrateFeedback(getApplicationContext());
                closeFloatMenu();

                // save cache with position and information about the location
                if (markerOptions != null) {
                    String name = locationXml.name;
                    String details = locationXml.address;
                    Utils.createCheckin(name, details, markerOptions.getPosition(), this);

                    Toast.makeText(this, "Seu check-in foi concluído com sucesso!", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(this, "Oops.. houve um erro ao realizar o check-in.", Toast.LENGTH_LONG).show();

                break;

        }
    }

    // set address to TextViews (street name and state)
    private void setAddress(Location mLocation) {

        if (mLocation != null) {

            double latitude = mLocation.getLatitude();
            double longitude = mLocation.getLongitude();
            LocationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext(), new GeocoderHandler());
        }

    }

    protected void onResume() {
        super.onResume();

        setUpMapIfNeeded();
    }

    private synchronized void gettingInformations() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    private void zoomInCurrentLocation() {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
    }

    public void onMapClick(LatLng latLng) {

        closeFloatMenu();

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

        zoomInCurrentLocation();

        location = new Location(locationManager.NETWORK_PROVIDER);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        setAddress(location);
    }

    private class GeocoderHandler extends Handler {
        public void handleMessage(Message message) {

            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }

            String[] address = locationAddress.split(";");

            try {
                streetTextView.setText(address[0]);
                stateTextView.setText(address[1]);
            } catch (Exception e) {
                e.printStackTrace();
                streetTextView.setText("- - -");
                stateTextView.setText("- - -");
            }


            // write location name—
            locationXml.name = streetTextView.getText().toString();
            locationXml.address = stateTextView.getText().toString();

        }
    }

    private void closeFloatMenu() {
        FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        if (menu.isExpanded())
            menu.collapse();
    }

}
