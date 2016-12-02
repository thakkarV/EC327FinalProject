package oops.com.report_a_potty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.TextKeyListener;
import android.view.KeyEvent;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class enterAddressActivity extends AppCompatActivity
{

    public final static String EXTRA_MESSAGE = "com.example.report_a_potty.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_address);
        final Button buttonGO = (Button) findViewById(R.id.addressEnter);
        final EditText addressInputBox = (EditText) findViewById(R.id.addressInput);

        // first we set the go button on click listener
        buttonGO.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {sendMessage(v, addressInputBox);}
        });

        // now the user should also be able to hit enter on the key board to proceed
        // for this we set the keyboard to a listener as well
        addressInputBox.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyID, KeyEvent keyEvent) {
                if (keyID == KeyEvent.KEYCODE_ENTER) {
                    sendMessage(v, addressInputBox);
                    return true;
                }
                return false;
            }
        });
    }

    // Called when go button is pressed or the keyboard enter key is pressed
    public void sendMessage(View v, EditText addressInputBox)
    {
        Intent intent = new Intent(this, locationActivity.class);
        String message = addressInputBox.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}