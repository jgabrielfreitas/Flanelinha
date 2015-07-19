package br.com.manta.database;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by JGabrielFreitas on 08/06/15.
 */
public class LocationObjectBase {

    private String latitude;
    private String longitude;
    private String dateHour;
    private String address;

    public LocationObjectBase() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        dateHour = simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateHour() {
        return dateHour;
    }

    public String toString() {
        return "LocationObjectBase{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", dateHour='" + dateHour + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
