package br.com.manta.mantaray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import br.com.manta.activity.MainActivity;
import br.com.manta.database.LocationDAO;
import br.com.manta.database.LocationObjectBase;
import br.com.manta.informations.LocationXml;
import br.com.manta.informations.UserCurrentPlace;
import br.com.manta.services.GPSTracker;

/**
 * Created by JGabrielFreitas on 01/01/15.
 */
public class Utils extends Application {

    public static final String CACHE_LAST_CHECKIN = "LAST_CHECKIN.xml";
    public static final String URL_IN_GITHUB = "https://github.com/jgabrielfreitas/Flanelinha";
    public static       String PACKAGE_NAME;

    public static boolean isConnected = false;

    private static UserCurrentPlace currentPlace;

    public static UserCurrentPlace getCurrentPlace() {
        return currentPlace;
    }

    public static void setCurrentPlace(UserCurrentPlace currentPlace) {
        Utils.currentPlace = currentPlace;
    }

    // create cache with name and content
    public static void createCache(final String nameCache, final String toSave, final Context context, final String classRequest) {

        new Thread() {
            public void run() {
                super.run();
                try {

                    FileOutputStream fos = context.openFileOutput(nameCache, context.MODE_PRIVATE);
                    PrintStream pos = new PrintStream(fos);
                    pos.print(toSave);
                    pos.close();

                    Log.i("CACHE_LOCATION", nameCache + " was saved successfully.\nCache saved:\n" + toSave);

                } catch (Exception e) {
                    Log.e("CACHE_LOCATION", "erro in createCache() - called by " + classRequest);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // Create a fake currentLocation for tests
    public static Location createFakeLocation() {
        Location fakeLocation = new Location("flp");
        fakeLocation.setLatitude(-22.93755403);
        fakeLocation.setLongitude(-43.35825153);
        fakeLocation.setAccuracy(3.0f);
        return fakeLocation;
    }

    // return coordinates from the last check-in
    public static LocationXml getInformationsAboutLastLocationFromCache(Context context) {

        if (justCheckFileCache(Utils.CACHE_LAST_CHECKIN)) {

            LocationXml lastLocation = null;

            try {

                FileInputStream fileInputStream = context.openFileInput(Utils.CACHE_LAST_CHECKIN);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = null;
                String savedCache = null;

                while ((line = bufferedReader.readLine()) != null)
                    if (savedCache == null)
                        savedCache = line;
                    else
                        savedCache += "\n" + line;

                XStream xstream = new XStream(new DomDriver());

                xstream.processAnnotations(new Class[]{ LocationXml.class });

                String XML = savedCache;
                lastLocation = (LocationXml) xstream.fromXML(XML);

            } catch (Exception e) {
                Toast.makeText(context, context.getString(R.string.register_error), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return lastLocation;
        } else {
            Log.e("stone_erro", "file not found or don't created");
            return null;
        }
    }

    // check if cache exists
    public static boolean justCheckFileCache(String cache) {

        File file = new File("/data/data/"+ PACKAGE_NAME +"/files/" + cache);

        if (file.exists())
            return true;
        else
            return false;
    }

    // save cache with current coordinates
    public static void createCheckin(String name, String details, LatLng location, Activity activity){

        try {

            LocationXml lastLocation = new LocationXml();
            lastLocation.latitude  = location.latitude;
            lastLocation.longitude = location.longitude;
            lastLocation.name      = name;
            lastLocation.address   = details;

            XStream xstream = new XStream(new DomDriver());
            xstream.processAnnotations(new Class[]{LocationXml.class});

            String dataToSave = xstream.toXML(lastLocation);

            createCache(Utils.CACHE_LAST_CHECKIN, dataToSave, activity.getApplicationContext(), activity.getClass().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // return current currentLocation
    public static Location getClientLocation(Context context){

        Location currentLocation = new Location("dummyprovider");

        GPSTracker gps = new GPSTracker(context);
        if(gps.canGetLocation())  // gps enabled | return boolean
            currentLocation = gps.getLocation();

        return currentLocation;

    }

    // not used
    public static void markLastLocationInGoogleMap(LatLng latLng, Activity activity, boolean debug) {

        if(!Utils.justCheckFileCache(Utils.CACHE_LAST_CHECKIN))
            return;

        LatLng latLngDebug;
        if (debug) {

            Location testLocation = Utils.createFakeLocation();   // for tests... [FAKE LOCATION]
            latLngDebug = new LatLng(testLocation.getLatitude(), testLocation.getLongitude());
            latLng = latLngDebug; // mark in map the currentLocation
            Log.i("Checkin","the last coordinates were recovered");
        } else {

            LocationXml testLocation = Utils.getInformationsAboutLastLocationFromCache(activity); // get coordinates from last check-in
            latLngDebug = new LatLng(testLocation.latitude, testLocation.longitude);
            latLng = latLngDebug; // mark in map the currentLocation
            Log.i("Checkin","the last coordinates were recovered");
        }
    }

    // just shake device
    public static void vibrateFeedback(Context context){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
    }

    // delete cache
    public static void deleteCache(String cache){

        if(justCheckFileCache(cache)) {
            File fileCache = new File("/data/data/"+ PACKAGE_NAME +"/files/" + cache);
            fileCache.delete();
        }
    }

    // show AlertDialog to enabled GPS
    public static void buildAlertMessageNoGps(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Localização")
                .setMessage("Ooops..Parece que o seu GPS está desligado, gostaria de ligar agora?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        activity.finish();
                        Toast.makeText(activity.getApplicationContext(), "Desculpe, mas precisamos da sua localização", Toast.LENGTH_LONG).show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    // do ping Google, (check internet)
    public static void doPing() {
        new Thread() {
            public void run() {
                super.run();
                String str = "";
                try {
                    Process process = Runtime.getRuntime().exec("/system/bin/ping -c 3 -s 1 " + "www.google.com.br"); // ping 3 packages
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    int i;
                    char[] buffer = new char[4096];
                    StringBuffer output = new StringBuffer();
                    while ((i = reader.read(buffer)) > 0)
                        output.append(buffer, 0, i);
                    reader.close();

                    str = output.toString();
                    Log.e("PING-ANDORID", "\n" + str);
                    isConnected = true;
                } catch (Exception e) {
                    Log.e("PING-ANDORID", "Ping failed");
                    e.printStackTrace();
                    isConnected = false;
                }
            }
        }.start();
    }

    // show AlertDialog to enabled GPS
    public static void buildAlertMessageNoNetwork(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Ooops.. ")
                .setMessage("Parece que a sua internet não está funcionando, gostaria de ligar agora?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        activity.finish();
                        Toast.makeText(activity.getApplicationContext(), "Desculpe, mas precisamos da sua localização", Toast.LENGTH_LONG).show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    // get User location
    public static synchronized Location getLocation(LocationManager mLocationManager1) {

        try {
            String provider = mLocationManager1.NETWORK_PROVIDER;
            Location location = mLocationManager1.getLastKnownLocation(provider);

            return location;
        } catch (Exception e) {
            return null;
        }
    }

    // method responsible for restart application if any exception throw
    public static void reliefValve(Activity activityRequest){
        Intent intent = new Intent(activityRequest, MainActivity.class);
        activityRequest.startActivity(intent);
        activityRequest.finish();
    }

}

