package oops.com.report_a_potty;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // make the button classes
        Button buttonGPS = (Button) findViewById(R.id.buttonGPS);
        Button buttonEnterAddress = (Button) findViewById(R.id.buttonEnterAddress);

        // now if the GPS button was clicked go straight  to the map activity and use GPS location
        // this  uses the last known location API to geolocate the phone
        buttonGPS.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Intent activityChangeIntent = new Intent(MainActivity.this, locationActivity.class);
                    startActivity(activityChangeIntent);
                }
            }
        );

        // if the enter address button was clicked then go to a seconday UI first that prompts user
        // to enter an address in a edit text view. this uses geocode to translate address to LatLng
        buttonEnterAddress.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // if the
                    Intent activityChangeIntent = new Intent(MainActivity.this, enterAddressActivity.class);
                    startActivity(activityChangeIntent);
                }
            }
        );
    }
}
/*
    public View.OnClickListener buttonClick = new View.OnClickListener()
    {
        public void onClick(View view)
        {
            // do something when the buttons are clicked
            switch(view.getId())
            {
                case R.id.ButtonGPS :
                {
                    break;
                }
                case R.id.ButtonEnterAddress :
                {
                    break;
                }
            }
        }
    };
*/