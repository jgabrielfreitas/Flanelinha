package br.com.manta.mantaray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Vibrator;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.*;
import android.view.MenuItem;
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

import br.com.manta.informations.LocationXml;
import br.com.manta.services.GPSTracker;

/**
 * Created by JGabrielFreitas on 01/01/15.
 */
public class Utils {

    public static final String CACHE_LAST_CHECKIN = "LAST_CHECKIN.xml";
    public static final String URL_IN_GITHUB = "https://github.com/jgabrielfreitas/Flanelinha";
    public static       String PACKAGE_NAME;

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

                FileInputStream fis = context.openFileInput(Utils.CACHE_LAST_CHECKIN);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String linha = null;
                String cache_salvo = null;

                while ((linha = br.readLine()) != null)
                    if (cache_salvo == null)
                        cache_salvo = linha;
                    else
                        cache_salvo += "\n" + linha;

                XStream xstream = new XStream(new DomDriver());

                xstream.processAnnotations(new Class[]{ LocationXml.class });

                String XML = cache_salvo;
                lastLocation = (LocationXml) xstream.fromXML(XML);

            } catch (Exception e) {
                Toast.makeText(context, "Erro na leitura do Cadastro", Toast.LENGTH_LONG).show();
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
    public static void createCheckin(String name, String details, Location location, Activity activity){

        try {

            LocationXml lastLocation = new LocationXml();
            lastLocation.latitude  = location.getLatitude();
            lastLocation.longitude = location.getLongitude();
            lastLocation.name      = name;
            lastLocation.address   = details;

            XStream xstream = new XStream(new DomDriver());
            xstream.processAnnotations(new Class[] { LocationXml.class });

            String dados = xstream.toXML(lastLocation);

            createCache(Utils.CACHE_LAST_CHECKIN, dados, activity.getApplicationContext(), activity.getClass().toString());

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


}

