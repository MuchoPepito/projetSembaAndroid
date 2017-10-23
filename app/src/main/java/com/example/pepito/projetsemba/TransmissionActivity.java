package com.example.pepito.projetsemba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TransmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmission);

        //TextView fileNameTextView = (TextView) findViewById(R.id.fileName);
        //fileNameTextView.setText("Fichier à transmettre");

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setScaleY(3f);
        progressBar.setProgress(65);

    }
}
