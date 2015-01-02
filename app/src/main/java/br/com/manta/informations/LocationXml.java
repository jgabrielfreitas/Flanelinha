package br.com.manta.informations;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by JGabrielFreitas on 02/01/2015 - 17:25.
 */
@XStreamAlias("Location")
public class LocationXml {

    @XStreamAlias("Latitude")
    public double latitude;

    @XStreamAlias("Longitude")
    public double longitude;

    @XStreamAlias("name")
    public String name;

    @XStreamAlias("address")
    public String address;

}
