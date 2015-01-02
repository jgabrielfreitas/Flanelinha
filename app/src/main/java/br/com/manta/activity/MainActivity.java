package br.com.manta.activity;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import br.com.manta.adapter.ItemAdapter;
import br.com.manta.informations.LocationXml;
import br.com.manta.mantaray.MenuItem;
import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    List<MenuItem> optionsArray = new ArrayList<>();
    ListView       listViewMenu;
    ItemAdapter    adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instanceViews();
    }

    private void instanceViews() {

        listViewMenu = (ListView) findViewById(R.id.mainActivityMenuListView);

        optionsArray.add(new MenuItem(getResources().getDrawable(R.drawable.marker_flanelinha), "Realizar checkin", "Marque aqui onde está o seu carro"));
        optionsArray.add(new MenuItem(getResources().getDrawable(R.drawable.flag)             , "Realizar busca"  , "Mostrar caminho até seu carro"));
        optionsArray.add(new MenuItem(getResources().getDrawable(R.drawable.about)            , "Sobre"  ,          "Informações do aplicativo"));

        adapter  = new ItemAdapter(optionsArray, getApplicationContext());

        listViewMenu.setAdapter(adapter);
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
        //Location testLocation = Utils.createFakeLocation();
        if(Utils.justCheckFileCache(Utils.CACHE_LAST_CHECKIN)) {
            LocationXml testLocation = Utils.getInformationsAboutLastLocation(this);
            LatLng latLngDebug = new LatLng(testLocation.latitude, testLocation.longitude); // create a fake location
            CheckinActivity.lastCoordinates = latLngDebug; // mark in map the fake location
        }

        Utils.getClientLocation(getApplicationContext()); // get user's location
        CheckinActivity.location = Utils.currentLocation; // set location to map


    }
}
