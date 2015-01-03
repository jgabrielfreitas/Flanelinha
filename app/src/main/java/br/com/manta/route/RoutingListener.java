package br.com.manta.route;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by JGabrielFreitas on 03/01/15.
 *
 * CREDIT: jd-alexander, ksarmalkar, licryle and bitdeli-chef - thanks you for creating the lib 'Google-Directions-Android' on GitHub
 * https://github.com/jd-alexander/Google-Directions-Android
 *
 */
public interface RoutingListener {
    public void onRoutingFailure();

    public void onRoutingStart();

    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route);
}