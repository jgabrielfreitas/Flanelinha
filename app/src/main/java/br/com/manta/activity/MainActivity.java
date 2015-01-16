package br.com.manta.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    List<MenuItem>  optionsArray = new ArrayList<>();
    ListView        listViewMenu;
    ItemAdapter     adapter;
    LocationManager locationManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setContentView(R.layout.activity_main);

        instanceViews();
    }

    private void instanceViews() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        listViewMenu = (ListView) findViewById(R.id.mainActivityMenuListView);

        optionsArray.add(new MenuItem(getResources().getDrawable(R.drawable.marker_flanelinha), "Realizar check-in", "Marque aqui onde está o seu carro"));
        optionsArray.add(new MenuItem(getResources().getDrawable(R.drawable.flag)             , "Realizar busca"   , "Mostrar caminho até seu carro"));
        optionsArray.add(new MenuItem(getResources().getDrawable(R.drawable.about)            , "Sobre"            , "Informações do aplicativo"));

        adapter  = new ItemAdapter(optionsArray, getApplicationContext());

        listViewMenu.setAdapter(adapter);
        listViewMenu.setOnItemClickListener(this);

        Utils.PACKAGE_NAME = getApplication().getPackageName();
    }

    private void doIntent(Class mClass){
        Intent intent = new Intent(this, mClass);
        startActivity(intent);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {

            case 0:
                validateAndGetLocation();

                if(CheckinActivity.location == null) {
                    Toast.makeText(this, "Não foi possível obter a sua localização.\nVocê está com o GPS ligado?", Toast.LENGTH_LONG).show();
                    validateAndGetLocation();
                    return;
                }

                if(!Utils.justCheckFileCache(Utils.CACHE_LAST_CHECKIN))
                    doIntent(CheckinActivity.class);
                else
                    createAlertDialog();
                break;
            case 1:
                if(!Utils.justCheckFileCache(Utils.CACHE_LAST_CHECKIN)) {
                    Toast.makeText(this, "Nenhuma localização anterior foi encontrada.\nVocê já realizou o check-in?", Toast.LENGTH_LONG).show();
                    return;
                }

                FindCarActivity.cacheLocation = Utils.getInformationsAboutLastLocationFromCache(getApplicationContext()); // from cache
                FindCarActivity.currentLocation = Utils.getClientLocation(getApplicationContext()); // current currentLocation
                doIntent(FindCarActivity.class);
                break;
            case 2:
                doIntent(AboutApplicationActivity.class);
                break;
        }
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

        CheckinActivity.location = Utils.getLocation(locationManager);
        Log.i("Location","current location was successfully captured");
    }


    private void createAlertDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.warning_checkin))
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.deleteCache(Utils.CACHE_LAST_CHECKIN); // delete old cache
                        doIntent(CheckinActivity.class); // set user to do a new check-in
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();

    }
}
