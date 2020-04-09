package com.example.gmcompany;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class act_CO2Senser extends AppCompatActivity {
    private String density;
    private String state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_co2sensor);
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        String[] trans = message.split(";");
        for (int i = 0;i < trans.length;i++){
            if (trans[i].equals("density")){
                density = trans[i+1];
            }
            if (trans[i].equals("state")){
                state = trans[i+1];
            }
        }
    }
}
