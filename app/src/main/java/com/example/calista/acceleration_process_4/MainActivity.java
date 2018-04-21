package com.example.calista.acceleration_process_4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button b_start;
    Button b_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_start = (Button) findViewById(R.id.b_start);
        b_stop = (Button) findViewById(R.id.b_stop);

        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("onclick", "started fall detection service");
                startService(new Intent(MainActivity.this, AccelerationProcessing.class));

            }
        });

        b_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(MainActivity.this, AccelerationProcessing.class));
            }
        });
    }
}
