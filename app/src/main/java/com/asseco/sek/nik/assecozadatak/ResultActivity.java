package com.asseco.sek.nik.assecozadatak;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity that shows result. Activity reads Result from Intent that had started activity.
 * Expected data intent are String with following keys: url, hash and storage.
 * Has button back that shuts down activity.
 */
public class ResultActivity extends AppCompatActivity {

    TextView urlView;
    TextView hashView;
    TextView storeView;

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();

        String url = intent.getStringExtra("url");
        if(url == null) url = "";
        String hash = intent.getStringExtra("hash");
        if(hash == null) hash = "";
        String storage = intent.getStringExtra("storage");
        if(storage == null) storage = "";

        urlView = (TextView) findViewById(R.id.url);
        hashView = (TextView) findViewById(R.id.hash);
        storeView = (TextView) findViewById(R.id.store);

        urlView.setText(url);
        hashView.setText(hash);
        storeView.setText(storage);


        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
