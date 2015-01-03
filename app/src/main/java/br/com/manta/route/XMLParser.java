package br.com.manta.route;

import android.util.Log;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by JGabrielFreitas on 03/01/15.
 *
 * CREDIT: jd-alexander, ksarmalkar, licryle and bitdeli-chef - thanks you for creating the lib 'Google-Directions-Android' on GitHub
 * https://github.com/jd-alexander/Google-Directions-Android
 *
 */
public class XMLParser {

    // names of the XML tags
    protected static final String MARKERS = "markers";
    protected static final String MARKER = "marker";

    protected URL feedUrl;

    protected XMLParser(final String feedUrl) {
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            Log.e("Routing Error", e.getMessage());
        }
    }

    protected InputStream getInputStream() {
        try {
            return feedUrl.openConnection().getInputStream();
        } catch (Exception e) {
            Log.e("Routing Error", e.getMessage());
            return null;
        }
    }
}
