package br.com.manta.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;

public class CheckinActivity extends ActionBarActivity implements View.OnClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private Button    checkinButton;
    private TextView  noteTextView;
    EditText localNameEditText;
    EditText detailsLocalEditText;

    public static Location location;  // currentLocation

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        setUpMapIfNeeded();
        instanceViews();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void instanceViews() {
        noteTextView  = (TextView) findViewById(R.id.noteTextView);
        checkinButton = (Button) findViewById(R.id.checkinButton);

        localNameEditText    = (EditText) findViewById(R.id.localNameEditText);
        detailsLocalEditText = (EditText) findViewById(R.id.detailsLocalEditText);

        noteTextView.setText(Html.fromHtml(getString(R.string.note)));
        checkinButton.setOnClickListener(this);
        googleMap.setOnMapClickListener(this);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null)
                setUpMap();
        }
    }

    private void setUpMap() {

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        googleMap.setMyLocationEnabled(true);
    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void onClick(View v) {

        Utils.vibrateFeedback(getApplicationContext());

        if(location != null) {
            String name = localNameEditText.getText().toString();
            String details = detailsLocalEditText.getText().toString();
            Utils.createCheckin(name, details, location, this);

            Toast.makeText(this, "Seu check-in foi concluído com sucesso!",Toast.LENGTH_LONG).show();
            this.finish();
        } else
            Toast.makeText(this, "Oops.. houve um erro ao realizar o check-in.",Toast.LENGTH_LONG).show();
    }

    public void onMapClick(LatLng latLng) {
        Intent intent = new Intent(this, DetailsMap.class);
        DetailsMap.currentLocation = location;
        startActivity(intent);
    }

}
