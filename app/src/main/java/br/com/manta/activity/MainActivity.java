package br.com.manta.activity;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import br.com.manta.mantaray.FakeLocation;
import br.com.manta.mantaray.R;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ListView listViewMenu;
    List<String> optionsArray = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instanceViews();
    }

    private void instanceViews() {

        listViewMenu = (ListView) findViewById(R.id.mainActivityMenuListView);

        optionsArray.add("Realizar check-in");
        optionsArray.add("Realizar busca");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                                                               android.R.layout.simple_list_item_1,
                                                               optionsArray);
        listViewMenu.setAdapter(arrayAdapter);
        listViewMenu.setOnItemClickListener(this);
    }

    private void doIntent(Class mClass){
        Intent intent = new Intent(this, mClass);
        startActivity(intent);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position){

            case 0:
                doIntent(CheckinActivity.class);
                break;
            case 1:

                break;
        }
    }

    protected void onResume() {
        super.onResume();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);
        LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude()); // latLng: -22.92655203 , -43.25726123

//        if (latLng != null)
//            CheckinActivity.lastCoordinates = latLng;


        // for tests... [FAKE LOCATION]
        Location testLocation = FakeLocation.createLocation(FakeLocation.LAT, FakeLocation.LNG, FakeLocation.ACCURACY);
        LatLng latLngDebug = new LatLng(testLocation.getLatitude(), testLocation.getLongitude()); // create a fake location
        CheckinActivity.lastCoordinates = latLngDebug; // mark in map the fake location

    }
}
