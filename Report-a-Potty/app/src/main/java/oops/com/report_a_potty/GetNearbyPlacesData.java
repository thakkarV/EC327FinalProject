package oops.com.report_a_potty;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Vijay Thakkar on 11-Dec-16.
 */

public class GetNearbyPlacesData extends AsyncTask {
    String placesData;
    GoogleMap mMap;
    String endpointURL;

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            endpointURL = (String) params[1];
            downloadURL downloadUrl = new downloadURL();
            placesData = downloadUrl.readURL(endpointURL);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception except) {
            Log.d("GooglePlacesReadTask", except.toString());
        }
        return placesData;
    }

    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        jsonDataParser dataParser = new jsonDataParser();
        nearbyPlacesList =  dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);

            // now we have the data of a lot of difference places of this type
            // all we need to do is to store it
            // churn it through the algorithm we  make
            // and then display the first n results of that list of nearby locations
            // for example, we could just do
            //mMap.addMarker(markerOptions); here
        }
    }
}
