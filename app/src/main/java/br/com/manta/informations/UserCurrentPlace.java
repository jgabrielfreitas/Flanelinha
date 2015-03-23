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
public class UserCurrentPlace implements  ResultCallback<PlaceLikelihoodBuffer>{

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
        buildPeddingResult(createGoogleApiClient());
        logFlow("object created");
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

        logFlow("creating GoogleApliClient......");

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                                             .addApi(Places.GEO_DATA_API)
                                             .addApi(Places.PLACE_DETECTION_API)
                                             .build();

        connectGoogleApiClient();

        logFlow("GoogleApliClient created. Returning.......");
        return googleApiClient;
    }

    private void buildPeddingResult(GoogleApiClient googleApiClient) {

        logFlow("starting method buildPeddingResult()");

        PlaceFilter filter = new PlaceFilter();
        filter.getPlaceTypes();
        filter.getPlaceIds();

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, filter);

        result.setResultCallback(this);
    }

    public void onResult(PlaceLikelihoodBuffer likelyPlaces) {

        logFlow("starting method onResult()");

        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
            logFlow(String.format("Place to add: %s", placeLikelihood.getPlace().getName()));
            placeList.add(placeLikelihood.getPlace().freeze());
        }

        setCurrentPlace(placeList.get(0).freeze());
        setCoordinates(getCurrentPlace().getLatLng());
        likelyPlaces.release();

        logFlow("all places was listed");
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
