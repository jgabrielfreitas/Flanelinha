package br.com.manta.mantaray;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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
    public static Location currentLocation;

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

    public static Location createFakeLocation() {
        // Create a random location
        Location fakeLocation = new Location("flp");
        fakeLocation.setLatitude(-22.93755403);
        fakeLocation.setLongitude(-43.35825153);
        fakeLocation.setAccuracy(3.0f);
        return fakeLocation;
    }

    public static void hideKeyboard(final Activity activity) {

        Thread thread = new Thread() {
            public void run() {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

                View view = activity.getCurrentFocus();
                if (view == null)
                    return;

                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            ;
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
        }
    }

    public static LocationXml getInformationsAboutLastLocation(Context context) {

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

    public static boolean justCheckFileCache(String cache) {

        File file = new File("/data/data/br.com.manta.mantaray/files/" + cache);

        if (file.exists())
            return true;
        else
            return false;
    }

    public static void createCheckin(Location location, Activity activity){

        try {

            LocationXml lastLocation = new LocationXml();
            lastLocation.latitude  = location.getLatitude();
            lastLocation.longitude = location.getLongitude();

            XStream xstream = new XStream(new DomDriver());
            xstream.processAnnotations(new Class[] { LocationXml.class });

            String dados = xstream.toXML(lastLocation);

            createCache(Utils.CACHE_LAST_CHECKIN, dados, activity.getApplicationContext(), activity.getClass().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getClientLocation(Context context){

        currentLocation = new Location("dummyprovider");
        GPSTracker gps = new GPSTracker(context);
        if(gps.canGetLocation()) { // gps enabled | return boolean
            currentLocation.setLatitude(gps.getLatitude());
            currentLocation.setLongitude(gps.getLongitude());
        }


    }

}

