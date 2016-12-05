package oops.com.report_a_potty;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
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
import java.util.List;
import java.util.Locale;





public class locationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
    public void onMapReady(GoogleMap googleMap)
    {
        // we make the map variable first
        mMap = googleMap;

        // first we get the string address from the enter address activity
        Intent intent = getIntent();
        final String currentStringAddress = intent.getStringExtra(enterAddressActivity.EXTRA_MESSAGE);

        // now use geocoder to get the LatLng
        //LatLng currentLatLngAddress = getCoordinatesFromAddress(Context.CONTEXT_IGNORE_SECURITY, currentStringAddress);



        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    
    // we make a wrapper function for getting the geocoded LatLng from the string address
    public LatLng getCoordinatesFromAddress(Context appContext, String addressString) {
        LatLng outputLatLng = null;
        Geocoder geocoder = new Geocoder(appContext, Locale.getDefault());
        List< Address > geocoderResults = null;
        try {
            // get results form geocoder first
            geocoderResults = geocoder.getFromLocationName(addressString, 1);
            while (geocoderResults.size()==0) {
                geocoderResults = geocoder.getFromLocationName(addressString, 1);
            }
            if (geocoderResults.size()>0) {
                Address address = geocoderResults.get(0);
                outputLatLng = new LatLng(address.getLatitude(),address.getLongitude());
            }
        } // look for any excpetions
        catch (Exception except) {
            System.out.print(except.getMessage());
        }
        // return that latitude and longitude
        return outputLatLng;
    }
}
