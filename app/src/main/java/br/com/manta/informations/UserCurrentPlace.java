package br.com.manta.informations;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JGabrielFreitas on 21/03/15.
 */
public class UserCurrentPlace {

    private Activity activity;
    GoogleApiClient googleApiClient;
    List<Place> placeList = new ArrayList<>();
    private Place currentPlace;
    private LatLng coordinates;
    private static final String TAG = "USER";

    public UserCurrentPlace() {
        super();
    }

    public UserCurrentPlace(Activity activity) {
        super();
        this.activity = activity;
        createGoogleApiClient();
    }

    public Place getCurrentPlace() {
        return currentPlace;
    }

    public void setCurrentPlace(Place currentPlace) {
        this.currentPlace = currentPlace.freeze();
        logFlow("> Place found: " + currentPlace);
    }


    public Activity getActivity() {
        return activity;
    }

    private GoogleApiClient createGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                                             .addApi(Places.GEO_DATA_API)
                                             .addApi(Places.PLACE_DETECTION_API)
                                             .build();

        connectGoogleApiClient();

        return googleApiClient;
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public List<Place> getPlaceList(){
        return this.placeList;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    private void logFlow(String flow) {
        Log.i(TAG, flow);
    }

    private void connectGoogleApiClient(){
        googleApiClient.connect();
    }

    public void disconnectGoogleApiClient(){
        googleApiClient.disconnect();
    }

}
