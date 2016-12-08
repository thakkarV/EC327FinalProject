package oops.com.report_a_potty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class locationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Create the map variable
        mMap = googleMap;

        // Get the string address from the enter address box, if it exists, and the button code
        Intent intent = getIntent();
        final String currentStringAddress = intent.getStringExtra(enterAddressActivity.EXTRA_MESSAGE);

        // !! RACHEL'S CHANGES BEGIN 12/7/2016
        // declare the button code, as received from the MainActivity
        final char buttonCode = MainActivity.buttonCode;

        // Using the geocoder to get the LatLng. Keeping in mind, the current string address may be nothing...
        // This is where it gets messy. I had to pass the string to the buttonDecision since I'm not sure how to deal
        // with the intent.
        Context appCont = getApplicationContext();
        LatLng currentLatLngAddress = buttonDecision(buttonCode, appCont, currentStringAddress);

        // Add a marker at your location and move the camera to that location
        mMap.addMarker(new MarkerOptions().position(currentLatLngAddress).title("You Are Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLngAddress));
        //mMap.moveCamera(CameraUpdateFactory.zoomIn());
    }

    public LatLng buttonDecision(char buttonCode, Context appContext, String currentStringAddress) {
        if (buttonCode == 'A') {
            // Get the coordinates from the string entered in the enter address activity
            LatLng currentLatLngAddress = getCoordinatesFromAddress(appContext, currentStringAddress);
            return currentLatLngAddress;
        } else if (buttonCode == 'G') {
            // first check if  the GPS is alright to use or not
            final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            checkGPSStatus(locationManager, appContext);
            // Pass to GPS function (not written yet)
            //LatLng currentLatLngAddress = getCoordinatesFromGPS(appCont, currentStringAddress);
            //return currentLatLngAddress;
        }
        // just so it would compile. When the GPS button is clicked a pin shows up at Sydney.
        LatLng sydney = new LatLng(-34, 151);
        return sydney;
    }

    // Creating a wrapper function for getting the geocoded LatLng from the string address, if it exists
    public LatLng getCoordinatesFromAddress(Context appContext, String addressString) {
        LatLng outputLatLng = null;
        Geocoder geocoder = new Geocoder(appContext, Locale.getDefault());
        List<Address> geocoderResults = null;
        try {
            // get results form geocoder first
            geocoderResults = geocoder.getFromLocationName(addressString, 1);
            int counter = 0;
            while (geocoderResults.size() == 0 && counter < 3) {
                geocoderResults = geocoder.getFromLocationName(addressString, 1);
                counter++;
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
    public void checkGPSStatus(LocationManager locationManager, Context appContext){
        if (! locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertNoGPS();
        }
    }

    private void alertNoGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Location services are disabled. Do you wish to enable them?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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
}
