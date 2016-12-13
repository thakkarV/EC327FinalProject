package oops.com.report_a_potty;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Vijay Thakkar on 11-Dec-16.
 */

public class GetNearbyPlacesData extends AsyncTask<Object,String,String> {
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
            placesData = "";
            placesData = downloadUrl.readURL(endpointURL);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
            //onPostExecute(placesData);
        } catch (Exception except) {
            Log.d("GooglePlacesReadTask", except.toString());
        }
        return placesData;
    }

    @Override
    protected void onPostExecute(String placesData) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        jsonDataParser dataParser = new jsonDataParser();
        nearbyPlacesList =  dataParser.parse(placesData);
        // this is where we return the list of the nearby locations
        // basically modify the ShowNearbyPlaces method such taht it returns the entire hashmap
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {

        if (nearbyPlacesList.size() != 0)
        {
            int counter = 0;
            //for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            while (counter < 7 || counter < nearbyPlacesList.size())  {
                HashMap<String, String> googlePlace = nearbyPlacesList.get(counter);

                // first convert the string LatLng to doubles
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));

                // place name will be the title of the marker
                String placeName = googlePlace.get("place_name");

                // place address is vicinity
                String vicinity = googlePlace.get("vicinity");

                // and the latitude and the loongitude
                LatLng latLng = new LatLng(lat, lng);

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(placeName)
                        .snippet(vicinity)
                        .visible(true));

                counter++;
            }
        }
        else
        {
            Log.d("onPostExecute: ","No markers to show");
            return;
        }
    }
}
