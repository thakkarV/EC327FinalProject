package oops.com.report_a_potty;

import android.*;
import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.support.v7.app.AppCompatActivity;

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Create the map variable
        mMap = googleMap;
        // first check for permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // first check for permissions
                checkFineLocationPermission();
                // if granted, start google play services
                GoogleApiClient locationClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
                locationClient.connect();
                // if we have permissions then start location manager
                final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                // check if GPS is online
                checkGPSStatus(locationManager);
                mMap.setMyLocationEnabled(true);
        } else {
            return;
        }
        mMap.clear();
                // declare the button code, as received from the MainActivity
        final char buttonCode = MainActivity.buttonCode;
        // Get the coordinates from the string entered in the enter address activity
        Context appCont = getApplicationContext();

        // Using the geocoder to get the LatLng. Keeping in mind, the current string address may be nothing...
        LatLng currentLatLngAddress = buttonDecision(buttonCode, appCont);
        // from here on the code should be inside button decision only since the activity forks into
        // two separate paths
        // Add a marker at your location and move the camera to that location
        Marker youAreHere = mMap.addMarker(new MarkerOptions().position(currentLatLngAddress).title("You Are Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLngAddress));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

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

    public LatLng buttonDecision(char buttonCode, final Context appContext) {
        if (buttonCode == 'A') {
            // Get the string address from the enter address box, if it exists, and the button code
            Intent intent = getIntent();
            final String currentStringAddress = intent.getStringExtra(enterAddressActivity.EXTRA_MESSAGE);

            // Using the geocoder to get the LatLng. Keeping in mind, the current string address may be nothing...
            LatLng currentLatLngAddress = getCoordinatesFromAddress(appContext, currentStringAddress);
            return currentLatLngAddress;
        } else { // buttonCode == 'G'
                       // Pass to GPS function
            LatLng currentLatLngAddress = getCoordinatesFromGPS(appContext);
            return currentLatLngAddress;
        }
    }

    // Creating a wrapper function for getting the geocoded LatLng from the string address, if it exists
    public LatLng getCoordinatesFromAddress(Context appContext, String addressString) {
        LatLng outputLatLng = null;
        Geocoder geocoder = new Geocoder(appContext, Locale.getDefault());

        List<Address> geocoderResults;

        try {
            // get results form geocoder first
            geocoderResults = geocoder.getFromLocationName(addressString, 1);
            while (geocoderResults.size() == 0) {
                geocoderResults = geocoder.getFromLocationName(addressString, 1);
            }
            if (geocoderResults.size() > 0) {
                Address address = geocoderResults.get(0);
                outputLatLng = new LatLng(address.getLatitude(), address.getLongitude());
            }
            /*
            else {
                String provider = LocationManager.GPS_PROVIDER;
                outputLatLng = new LatLng(LocationManager.getLastKnownLocation(provider));
            }
            */
        } // look for any exceptions
        catch (Exception except) {
            System.out.print(except.getMessage());
        }
        // return that latitude and longitude
        return outputLatLng;
    }

    // user might have location services disabled so we first check for that
    // first we get the location manager
    public void checkGPSStatus(LocationManager locationManager) {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertNoGPS();
        }
    }

    // if the location services are not enabled then prompt user to enable them
    private void alertNoGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Location services are disabled. Do you wish to enable them?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    // Implementing a location listener that will listen for location changes
    private LocationListener listener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            Context appContext = getApplicationContext();
            LatLng currentLatLngAddress = getCoordinatesFromGPS(appContext);
            System.out.print(currentLatLngAddress);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    public LatLng getCoordinatesFromGPS(Context appCont) {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        try {

            // Permission check - required by Android *cue eye roll*
            if (SDK_INT >= 23 && ContextCompat.checkSelfPermission(appCont, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                boolean permiss = true;

                if (permiss) {
                    //requestLocationUpdates(provider, min time, min distance, location listener)
                    locationManager.requestLocationUpdates(locationProvider, 4000L, 0f, listener);
                }

                if (locationManager != null) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    LatLng currentCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
                    return currentCoordinates;
                }
            }

            LatLng sydney = new LatLng(0, 0);
            return sydney;

        } catch (Exception except) {

        }

        LatLng sydney = new LatLng(0, 0);
        return sydney;
    }

    private final int LOCATION_PERMISSION_CODE = 12;
    private boolean checkFineLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_CODE);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_CODE);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE: {
                // arrays are empty if the request was cancelled
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // granted permission
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // buildGoogleApiClient();
                    }
                    mMap.setMyLocationEnabled(true);


                } else {

                    // Permission denied, Disable the functionality that depends on this permission
                }
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }
}
