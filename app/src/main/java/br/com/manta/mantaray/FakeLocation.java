package br.com.manta.mantaray;

import android.location.Location;

/**
 * Created by JGabrielFreitas on 01/01/15.
 */
public class FakeLocation {

    private static final String PROVIDER = "flp";
    public  static final double LAT = -22.93755403; // random location
    public  static final double LNG = -43.35825153;
    public  static final float  ACCURACY = 3.0f;

    public static Location createLocation(double lat, double lng, float accuracy) {
        // Create a new Location
        Location newLocation = new Location(PROVIDER);
        newLocation.setLatitude(lat);
        newLocation.setLongitude(lng);
        newLocation.setAccuracy(accuracy);
        return newLocation;
    }
}
