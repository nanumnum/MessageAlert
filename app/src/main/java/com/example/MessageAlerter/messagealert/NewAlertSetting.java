package com.example.MessageAlerter.messagealert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;


/**
 * Created by Jamie on 9/12/2015.
 */
public class NewAlertSetting extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alert_setting);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        final Context context = this;

        Button button = (Button) findViewById(R.id.btnUpdateSetting);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AlertControl ac = new AlertControl(context);
                ac.InitiateDatabase();

                EditText  txtMatchMessage = (EditText)findViewById(R.id.txtMatchMessage);
                ac.InsertNewAlertSettingDetails(txtMatchMessage.getText().toString(), "c:", 1  );

                Intent intent = new Intent(context, AlertList.class);
                startActivity(intent);

            }

        });

    }

}
