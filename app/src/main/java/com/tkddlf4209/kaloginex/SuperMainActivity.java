package com.tkddlf4209.kaloginex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Home on 02.12.2016.
 */

public class SuperMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_activity);

        Intent intent = new Intent(SuperMainActivity.this, MainActivity.class);
        startActivity(intent);

    }

}
