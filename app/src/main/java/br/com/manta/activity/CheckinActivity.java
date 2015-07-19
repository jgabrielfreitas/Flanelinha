package br.com.manta.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import com.google.android.gms.maps.model.PolylineOptions;

import br.com.manta.database.Local;
import br.com.manta.database.LocationDAO;
import br.com.manta.database.LocationObjectBase;
import br.com.manta.informations.LocationAddress;
import br.com.manta.informations.LocationXml;
import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;
import br.com.manta.route.Route;
import br.com.manta.route.Routing;
import br.com.manta.route.RoutingListener;

public class CheckinActivity extends ActionBarActivity implements View.OnClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    FloatingActionButton floatAbout;
    FloatingActionButton floatCheckin;
    FloatingActionButton floatFindCar;
    FloatingActionButton fab_car_founded;
    TextView streetTextView;
    TextView stateTextView;
    TextView yourCarTextView;
    MarkerOptions markerOptions;
    Marker marker;
    Location location;
    private String titleMarker = "Você está aqui!";
    private String titleNewMarker = "Local escolhido";
    private LocationManager locationManager;
    LocationXml locationXml = new LocationXml();
    public LocationXml cacheLocation; // checkin currentLocation
    private LatLng currentPlace = Utils.getCurrentPlace().getCurrentPlace().getLatLng();
    private boolean isFindingCar = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin2);

        gettingInformations();

        setUpMapIfNeeded();
        instanceViews();
    }

    private void instanceViews() {

        fab_car_founded = (FloatingActionButton) findViewById(R.id.fab_car_founded);

        streetTextView = (TextView) findViewById(R.id.streetTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        yourCarTextView = (TextView) findViewById(R.id.yourCarTextView);

        floatAbout = (FloatingActionButton) findViewById(R.id.fab_about);
        floatAbout.setIcon(R.drawable.ic_action_info);
        floatAbout.setSize(FloatingActionButton.SIZE_MINI);

        floatCheckin = (FloatingActionButton) findViewById(R.id.fab_menu_checkin);
        floatCheckin.setIcon(R.drawable.ic_action_room);
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
        markerOptions.position(Utils.getCurrentPlace().getPlaceList().get(0).getLatLng()).title(titleMarker);

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
//                    Intent intent_find = new Intent(this, FindCarActivity.class);
//                    startActivity(intent_find);
                    findCar();
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

                    // create the object to save in base
                    LocationObjectBase locationObjectBase = new LocationObjectBase();
                    locationObjectBase.setAddress(name);
                    locationObjectBase.setLatitude(String.valueOf(markerOptions.getPosition().latitude));
                    locationObjectBase.setLongitude(String.valueOf(markerOptions.getPosition().longitude));

                    // instance DAO and insert into database
                    LocationDAO locationDAO = new LocationDAO(this);
                    locationDAO.insert(locationObjectBase);

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

        // if any exception throw here,
        // go to MainActivity
        try {
            setUpMapIfNeeded();


            LocationDAO locationDAO = new LocationDAO(this);
            locationDAO.showAllColumnsInLog();
            for (Local local : locationDAO.getAllLocal()) {
                Log.e("LOCAL_DAO", local.toString());
            }

            if (locationDAO.getLocalCount() <= 0)
                Log.e("LOCAL_DAO", "THE BASE IS EMPTY");


        } catch (Exception e) {

            e.printStackTrace();
            Utils.reliefValve(this);
        }
    }

    private synchronized void gettingInformations() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    private void zoomInCurrentLocation() {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
    }

    public void onMapClick(LatLng latLng) {

        if (isFindingCar == true) {
            return;
        }

        yourCarTextView.setVisibility(View.INVISIBLE);
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

    /**
     * FindCarActivity was moved to here.
     * Using this way, the application use less network data.
     */

    private void findCar() {

        cacheLocation = Utils.getInformationsAboutLastLocationFromCache(this);
        RouteCreatorAsyncTask routeCreatorAsyncTask = new RouteCreatorAsyncTask();
        routeCreatorAsyncTask.execute();
    }

    private class RouteCreatorAsyncTask extends AsyncTask<Void, Void, Void> implements RoutingListener, View.OnClickListener {

        ProgressDialog dialog;

        protected void onPreExecute() {
            super.onPreExecute();

            isFindingCar = true;
            dialog = ProgressDialog.show(CheckinActivity.this, "Aguarde", getString(R.string.creating_route), false, false);
            yourCarTextView.setVisibility(View.VISIBLE);
            googleMap.clear();
        }

        protected Void doInBackground(Void... params) {

            // create and draw the route
            LatLng end = new LatLng(cacheLocation.latitude, cacheLocation.longitude);
            Routing routing = new Routing(Routing.TravelMode.WALKING);
            routing.registerListener(this);
            routing.execute(currentPlace, end);

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // add in google map the marker with check-in location
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(cacheLocation.latitude, cacheLocation.longitude)).title("Seu carro").snippet("Último check-in");

            // add in google map the user's current location
            MarkerOptions markerOptionsCurrent = new MarkerOptions();
            markerOptionsCurrent.position(currentPlace).title("Você").snippet("Ponto de partida");

            Marker marker = googleMap.addMarker(markerOptions);
            googleMap.addMarker(markerOptionsCurrent);
            marker.showInfoWindow();

            // set car location to textviews
            streetTextView.setText(cacheLocation.name);
            stateTextView.setText(cacheLocation.address);

            googleMap.setMyLocationEnabled(true);

            // show founded car FAB
            fab_car_founded.setVisibility(View.VISIBLE);
            fab_car_founded.setOnClickListener(this);
        }

        public void onRoutingFailure() {
        }

        public void onRoutingStart() {
        }

        public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {

            yourCarTextView.setVisibility(View.VISIBLE);
            PolylineOptions polyoptions = new PolylineOptions();
            polyoptions.color(getResources().getColor(R.color.router_color));
            polyoptions.width(15);
            polyoptions.addAll(mPolyOptions.getPoints());
            googleMap.addPolyline(polyoptions);

            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPlace, 15));
        }

        // fab_car_founded's onclick
        public void onClick(View v) {

            Utils.vibrateFeedback(CheckinActivity.this);
            Toast.makeText(CheckinActivity.this, "Concluído.", Toast.LENGTH_LONG).show();
            googleMap.clear();

            // add in google map the last position
            MarkerOptions markerLastPosition = new MarkerOptions();
            markerLastPosition.position(new LatLng(cacheLocation.latitude, cacheLocation.longitude)).title("Seu carro").snippet("Último check-in");
            marker = googleMap.addMarker(markerLastPosition);

            fab_car_founded.setVisibility(View.INVISIBLE);

            Utils.deleteCache(Utils.CACHE_LAST_CHECKIN);
            googleMap.setMyLocationEnabled(false);
            isFindingCar = false;

            zoomInCurrentLocation();
        }
    }

    public void onBackPressed() {

        if (isFindingCar == true) {
            fab_car_founded.setVisibility(View.INVISIBLE);
            yourCarTextView.setVisibility(View.INVISIBLE);
            googleMap.setMyLocationEnabled(false);
            googleMap.clear();

            markerOptions = new MarkerOptions();
            markerOptions.position(Utils.getCurrentPlace().getPlaceList().get(0).getLatLng()).title(titleMarker);

            marker = googleMap.addMarker(markerOptions);
            marker.showInfoWindow();

            zoomInCurrentLocation();

            setAddress(Utils.getLocation(locationManager));
            isFindingCar = false;
            return;
        }

        super.onBackPressed();
    }
}
