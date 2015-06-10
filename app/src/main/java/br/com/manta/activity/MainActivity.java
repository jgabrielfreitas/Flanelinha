package br.com.manta.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import br.com.manta.informations.UserCurrentPlace;
import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;

public class MainActivity extends Activity implements ResultCallback<PlaceLikelihoodBuffer> {

    LocationManager locationManager;
    TextView placeTextView;
    UserCurrentPlace userCurrentPlace;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setContentView(R.layout.activity_main);

        userCurrentPlace = new UserCurrentPlace(this);
        instanceViews();
    }

    // if disabled, create a dialog to enabled
    private boolean isGpsEnabled() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void instanceViews() {

        placeTextView = (TextView) findViewById(R.id.placeTextView);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Utils.PACKAGE_NAME = getApplication().getPackageName();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    // only for debug
    private void insertLocationsInBase() {

        return;
    }


    private void doIntent(Class <?> classToGo, boolean neededKill) {
        Intent intent = new Intent(this, classToGo);
        startActivity(intent);

        if(neededKill)
            finish();
    }

    private void buildPeddingResult() {

        PlaceFilter filter = new PlaceFilter();
        filter.getPlaceTypes();
        filter.getPlaceIds();

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(userCurrentPlace.getGoogleApiClient(), filter);

        result.setResultCallback(this);
    }

    public void onResult(PlaceLikelihoodBuffer likelyPlaces) {

        try {

            for (PlaceLikelihood placeLikelihood : likelyPlaces)
                userCurrentPlace.getPlaceList().add(placeLikelihood.getPlace().freeze());

            userCurrentPlace.setCurrentPlace(userCurrentPlace.getPlaceList().get(0).freeze());
            userCurrentPlace.setCoordinates(userCurrentPlace.getCurrentPlace().getLatLng());
            likelyPlaces.release();

            Utils.setCurrentPlace(userCurrentPlace);
            doIntent(CheckinActivity.class, true);
        } catch (Exception e) {
            e.printStackTrace();
            doIntent(LocationNotFoundActivity.class, false);
        }
    }

    protected void onResume() {
        super.onResume();

        insertLocationsInBase();


//        if (isGpsEnabled()) {
//            Utils.buildAlertMessageNoGps(this);
//            return;
//        }
//        buildPeddingResult();
    }
}
