package com.imperium.power.nfcmango;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {

    static String username;

    /**
     * Creates view and adds setOnClickListener for pkball image
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_screen);

            ImageView pkball = (ImageView) findViewById(R.id.pokeballClick);
            final EditText usernameField = (EditText) findViewById(R.id.unField);

            EditText password = (EditText) findViewById(R.id.pwField);
            password.setTransformationMethod(new PasswordTransformationMethod());

            pkball.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(!(usernameField.getText().toString().equals(""))){
                        username = usernameField.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), NFCScreen.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        TextView error = (TextView) findViewById(R.id.error_text);
                        error.setText(R.string.home_screen_error);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
