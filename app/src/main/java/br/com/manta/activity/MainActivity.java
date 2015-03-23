package br.com.manta.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;

public class MainActivity extends Activity  {

    LocationManager locationManager;
    private final String TAG = "MAIN";
    TextView placeTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Crashlytics.start(this);
        setContentView(R.layout.activity_main);
//        CheckinActivity.userPlace = new UserCurrentPlace(this);
        instanceViews();
    }

    private void instanceViews() {

        placeTextView = (TextView) findViewById(R.id.placeTextView);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Utils.PACKAGE_NAME = getApplication().getPackageName();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    }

    protected void onResume() {
        super.onResume();


//        if (true)
//            doIntent();

    }

    private void doIntent(){
        Intent intent = new Intent(this, CheckinActivity.class);
        startActivity(intent);
    }

}
