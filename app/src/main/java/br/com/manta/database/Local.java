package br.com.manta.database;

/**
 * Created by user on 16/01/2015.
 */
public class Local {

    public int id;
    public double latitude;
    public double longitude;
    public String localName;
    public String dateTime;

    public Local() {
    }

    public Local(int id, double latitude, double longitude, String localName, String dateTime) {
        this.id        = id;
        this.latitude  = latitude;
        this.longitude = longitude;
        this.localName = localName;
        this.dateTime  = dateTime;
    }


    public Local(double latitude, double longitude, String localName, String dateTime) {
        this.latitude  = latitude;
        this.longitude = longitude;
        this.localName = localName;
        this.dateTime  = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
