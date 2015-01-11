package br.com.manta.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.manta.mantaray.CustomScrollView;
import br.com.manta.mantaray.R;
import br.com.manta.mantaray.ResizeAnimation;
import br.com.manta.mantaray.Utils;

public class CheckinActivity extends ActionBarActivity implements View.OnClickListener, GoogleMap.OnMapClickListener,
                                                                  GoogleMap.OnMapLongClickListener {

    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    private Button    checkinButton;
    private TextView  noteTextView;
    private SupportMapFragment supportMapFragment;
    private CustomScrollView   checkinScrollView;
    EditText localNameEditText;
    EditText detailsLocalEditText;
    RelativeLayout relativeLayoutNote;
    LatLng currentPosition;
    Marker marker;
    MarkerOptions markerOptions;
    private boolean mMapViewExpanded = false;
    private String  titleMarker      = "Você está aqui!";
    private String  titleNewMarker   = "Local escolhido";
    public static Location location;  // user location
    Animation fadeIn;
    Animation fadeOut;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        setUpMapIfNeeded();
        instanceViews();

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeIn  = AnimationUtils.loadAnimation(this, R.anim.fade_in);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent) // Add all of this activity's parents to the back stack
                                                 .startActivities(); // Navigate up to the closest parent
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void instanceViews() {
        noteTextView  = (TextView) findViewById(R.id.noteTextView);
        checkinButton = (Button)   findViewById(R.id.checkinButton);
        checkinScrollView  = (CustomScrollView) findViewById(R.id.checkinScrollView);
        relativeLayoutNote = (RelativeLayout)   findViewById(R.id.relativeLayoutNote);


        localNameEditText    = (EditText) findViewById(R.id.localNameEditText);
        detailsLocalEditText = (EditText) findViewById(R.id.detailsLocalEditText);

        noteTextView.setText(Html.fromHtml(getString(R.string.note_not_expanded)));
        checkinButton.setOnClickListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            googleMap = supportMapFragment.getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null)
                setUpMap();
        }
    }

    private void setUpMap() {

        currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        markerOptions = new MarkerOptions();
        markerOptions.position(currentPosition).title(titleMarker).draggable(true);

        marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();

        zoomInCurrentLocation();
    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void onClick(View v) {

        // just vibrate device
        Utils.vibrateFeedback(getApplicationContext());

        // save cache with position and information about the location
        if(markerOptions != null) {
            String name    = localNameEditText.getText().toString();
            String details = detailsLocalEditText.getText().toString();
            Utils.createCheckin(name, details, markerOptions.getPosition(), this);

            Toast.makeText(this, "Seu check-in foi concluído com sucesso!",Toast.LENGTH_LONG).show();
            finish();
        } else
            Toast.makeText(this, "Oops.. houve um erro ao realizar o check-in.",Toast.LENGTH_LONG).show();
    }

    public void onMapClick(LatLng latLng) {

        // Creating a marker
        markerOptions = new MarkerOptions();

        // Setting the position for the marker
        markerOptions.position(latLng);

        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(titleNewMarker);

        // user can drag the marker
        markerOptions.draggable(true);

        // Clears the previously touched position
        googleMap.clear();

        // Animating to the touched position
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();

     }

    public void onMapLongClick(LatLng latLng) {

        Utils.vibrateFeedback(this);
        animateMapView();
    }

    private void animateMapView() {

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) supportMapFragment.getView().getLayoutParams();

        ResizeAnimation a = new ResizeAnimation(supportMapFragment.getView());
        a.setDuration(500);

        if (!getMapViewStatus()) {
            mMapViewExpanded = true;
            a.setParams(lp.height, dpToPx(getResources(), 400));
            checkinScrollView.setEnableScrolling(false);
            changeTextNote(Html.fromHtml(getString(R.string.note_expanded)));
            zoomInCurrentLocation();
        } else {
            checkinScrollView.setEnableScrolling(true);
            mMapViewExpanded = false;
            a.setParams(lp.height, dpToPx(getResources(), 150));
            changeTextNote(Html.fromHtml(getString(R.string.note_not_expanded)));
            zoomInCurrentLocation();
        }
        supportMapFragment.getView().startAnimation(a);
    }

    private boolean getMapViewStatus() {
        return mMapViewExpanded;
    }

    public int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    private void changeTextNote(Spanned note){
        relativeLayoutNote.startAnimation(fadeOut);
        noteTextView.startAnimation(fadeOut);
        noteTextView.setText(note);
        noteTextView.startAnimation(fadeIn);
        relativeLayoutNote.startAnimation(fadeIn);
    }

    private void zoomInCurrentLocation(){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
    }

    public void onBackPressed() {
        if (mMapViewExpanded)
            animateMapView();
        else
            super.onBackPressed();
    }
}
