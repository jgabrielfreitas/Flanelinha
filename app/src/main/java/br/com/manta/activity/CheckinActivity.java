package br.com.manta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.manta.mantaray.R;

public class CheckinActivity extends ActionBarActivity implements View.OnClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    FloatingActionButton floatAbout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin2);
        setUpMapIfNeeded();
        instanceViews();
    }

    private void instanceViews() {

        floatAbout = (FloatingActionButton) findViewById(R.id.fab_menu_about);
        floatAbout.setIcon(R.drawable.ic_action_info_outline);
        floatAbout.setSize(FloatingActionButton.SIZE_MINI);


        floatAbout.setOnClickListener(this);
    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    public void onClick(View view) {

        switch (view.getId()){

            case R.id.fab_menu_about:
                Intent intent = new Intent(this, AboutApplicationActivity.class);
                startActivity(intent);
                break;

        }
    }
}
