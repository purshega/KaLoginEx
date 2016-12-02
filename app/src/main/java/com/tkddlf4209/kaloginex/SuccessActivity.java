package com.tkddlf4209.kaloginex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity {

    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        textview = (TextView) findViewById(R.id.textView);
        textview.setText("CONNECT SUCCESS");


    }
}
