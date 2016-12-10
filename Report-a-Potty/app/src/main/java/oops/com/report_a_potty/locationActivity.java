package oops.com.report_a_potty;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.os.Build.VERSION.SDK_INT;

public class locationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Create the map variable
        mMap = googleMap;
        // first check for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // if granted, start google play services
                GoogleApiClient locationClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
                locationClient.connect();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            return;
        }
        mMap.clear();
        Context appCont = getApplicationContext();

        // Get the string address from the enter address box, if it exists, and the button code
        Intent intent = getIntent();
        final String currentStringAddress = intent.getStringExtra(enterAddressActivity.EXTRA_MESSAGE);

        // declare the button code, as received from the MainActivity
        final char buttonCode = MainActivity.buttonCode;

        // Using the geocoder to get the LatLng. Keeping in mind, the current string address may be nothing...
        LatLng currentLatLngAddress = buttonDecision(buttonCode, appCont, currentStringAddress);

        // Add a marker at your location and move the camera to that location
        Marker youAreHere = mMap.addMarker(new MarkerOptions().position(currentLatLngAddress).title("You Are Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLngAddress));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        // Add all of the markers for all of the public restrooms in our array thing
        // Algorithm
        /*
            for length of array of places we have or whatever
                mMap.addMarker(new MarkerOptions()
                    .position(wherever it is)
                    .title("Name of Place")
                    .snippet("About the Place")
                    .visible(true);
                    // The info window will show when clicked
         */
    }

    public LatLng buttonDecision(char buttonCode, final Context appContext, String currentStringAddress) {
        if (buttonCode == 'A') {
            // Get the coordinates from the string entered in the enter address activity
            LatLng currentLatLngAddress = GetLatLng.getCoordinatesFromAddress(appContext, currentStringAddress);
            return currentLatLngAddress;
        } else { // buttonCode == 'G'
            // first check if  the GPS is alright to use or not
            final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
<<<<<<< HEAD
            checkGPSStatus(locationManager);
            
=======
            GetLatLng.checkGPSStatus(locationManager);
>>>>>>> da699c5b69a06517a6a89024b47aba0a94d48b65
            // Pass to GPS function
            LatLng currentLatLngAddress = GetLatLng.getCoordinatesFromGPS(appContext);
            return currentLatLngAddress;
        }
    }
}
