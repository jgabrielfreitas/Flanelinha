package br.com.manta.activity;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import br.com.manta.informations.UserCurrentPlace;
import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;

public class MainActivity extends Activity  {

    LocationManager locationManager;
    private final String TAG = "MAIN";
    UserCurrentPlace userCurrentPlace;
    TextView placeTextView;
    GetLocation getLocation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Crashlytics.start(this);
        setContentView(R.layout.activity_main);

        instanceViews();
    }

    private void instanceViews() {

        placeTextView = (TextView) findViewById(R.id.placeTextView);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Utils.PACKAGE_NAME = getApplication().getPackageName();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        getLocation = new GetLocation();
        getLocation.execute();

    }

    protected void onResume() {
        super.onResume();
        validateAndGetLocation();
    }

    private void validateAndGetLocation() {

        if (userCurrentPlace != null) {

            Toast.makeText(this, userCurrentPlace.getCurrentPlace().getName(), Toast.LENGTH_LONG).show();

        } else
            Toast.makeText(this, "Erro na busca do local", Toast.LENGTH_LONG).show();

        // check if GPS is enabled
//        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Utils.buildAlertMessageNoGps(this); // if disabled, create a dialog to enabled
//            return;
//        }
    }

    // unused
//    public synchronized Location getLocation() {
//
//        try {
//
//            String provider   = locationManager.NETWORK_PROVIDER;
//            Location location = locationManager.getLastKnownLocation(provider);
//
//            return location;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    private class GetLocation extends AsyncTask<Void, UserCurrentPlace, UserCurrentPlace> {

        protected void onPreExecute() {
            userCurrentPlace = new UserCurrentPlace(getActivity());
        }

        protected UserCurrentPlace doInBackground(Void... params) {

            return userCurrentPlace;
        }

        protected UserCurrentPlace onPostExecute() {

            return userCurrentPlace;
        }
    }

    private Activity getActivity() {
        return MainActivity.this;
    }

}
