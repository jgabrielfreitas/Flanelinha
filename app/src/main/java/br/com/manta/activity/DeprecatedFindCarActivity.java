package br.com.manta.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import br.com.manta.informations.LocationXml;
import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;
import br.com.manta.route.Route;
import br.com.manta.route.Routing;
import br.com.manta.route.RoutingListener;

public class DeprecatedFindCarActivity extends ActionBarActivity implements View.OnClickListener {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    public  LocationXml cacheLocation; // checkin currentLocation

    private LatLng currentPlace = Utils.getCurrentPlace().getCurrentPlace().getLatLng();

    TextView nameLocationTextView;
    TextView detailsLocationTextView;
    Button   carFoundButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_car);

        cacheLocation = Utils.getInformationsAboutLastLocationFromCache(this);

        instanceViews();
        setUpMapIfNeeded();
    }

    private void instanceViews() {

        nameLocationTextView    = (TextView) findViewById(R.id.nameLocationTextView);
        detailsLocationTextView = (TextView) findViewById(R.id.detailsLocationTextView);
        carFoundButton          = (Button)   findViewById(R.id.carFoundButton);

        if(cacheLocation.name != null && !cacheLocation.name.isEmpty())
            nameLocationTextView.setText("Local :\n" + cacheLocation.name);
        else
            nameLocationTextView.setText("Local :\nNão informado.");

        if(cacheLocation.address != null && !cacheLocation.address.isEmpty())
            detailsLocationTextView.setText("Detalhes :\n" + cacheLocation.address);
        else
            detailsLocationTextView.setText("Detalhes :\nNão informado.");

        carFoundButton.setOnClickListener(this);

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

        RouteCreatorAsyncTask routeCreatorAsyncTask = new RouteCreatorAsyncTask();
        routeCreatorAsyncTask.execute();

    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }

    public void onClick(View v) {

        Utils.vibrateFeedback(this);
        Toast.makeText(this, "Concluído.", Toast.LENGTH_LONG).show();
        Utils.deleteCache(Utils.CACHE_LAST_CHECKIN);
        finish();
    }

    private class RouteCreatorAsyncTask extends AsyncTask<Void, Void, Void> implements RoutingListener{

        ProgressDialog dialog;

        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(DeprecatedFindCarActivity.this, "Aguarde", getString(R.string.creating_route), false, false);
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

            Marker marker = googleMap.addMarker(markerOptions);
            marker.showInfoWindow();

            // add in google map the user's current location
            MarkerOptions markerOptionsCurrent = new MarkerOptions();
            markerOptionsCurrent.position(currentPlace).title("Você").snippet("Ponto de partida");

            Marker markerCurrent = googleMap.addMarker(markerOptionsCurrent);
            markerCurrent.showInfoWindow();

            googleMap.setMyLocationEnabled(true);
        }

        public void onRoutingFailure() {}

        public void onRoutingStart() {}

        public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {
            PolylineOptions polyoptions = new PolylineOptions();
            polyoptions.color(getResources().getColor(R.color.router_color));
            polyoptions.width(15);
            polyoptions.addAll(mPolyOptions.getPoints());
            googleMap.addPolyline(polyoptions);

            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPlace, 15));
        }
    }


}