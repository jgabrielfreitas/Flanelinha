package br.com.manta.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import br.com.manta.adapter.ItemAdapter;
import br.com.manta.mantaray.MenuItem;
import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;

public class MainActivity extends Activity {

    LocationManager locationManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setContentView(R.layout.activity_main);

        instanceViews();
    }

    private void instanceViews() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Utils.PACKAGE_NAME = getApplication().getPackageName();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    protected void onResume() {
        super.onResume();
        validateAndGetLocation();
    }

    private void validateAndGetLocation() {

        // check if GPS is enabled
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Utils.buildAlertMessageNoGps(this); // if disabled, create a dialog to enabled
            return;
        }

        Intent intent = new Intent(this, CheckinActivity.class);
        startActivity(intent);
        finish();

    }


    public synchronized Location getLocation() {

        try {

            String provider   = locationManager.NETWORK_PROVIDER;
            Location location = locationManager.getLastKnownLocation(provider);

            return location;
        } catch (Exception e) {
            return null;
        }
    }
}
