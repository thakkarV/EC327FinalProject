package oops.com.report_a_potty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by Vijay Thakkar on 10-Dec-16.
 */

public class GetLatLng {
    // Creating a wrapper function for getting the geocoded LatLng from the string address, if it exists
    public static LatLng getCoordinatesFromAddress(Context appContext, String addressString) {
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
    public static void checkGPSStatus(LocationManager locationManager) {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alertNoGPS();
        }
    }

    // if the location services are not enabled then prompt user to enable them
    private static void alertNoGPS() {
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
    private static LocationListener listener = new LocationListener() {
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

    public static LatLng getCoordinatesFromGPS(Context appCont) {

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

}
