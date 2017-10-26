package com.example.pepito.projetsemba;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TransmissionActivity extends AppCompatActivity {

    private File fileToSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmission);

        //TextView fileNameTextView = (TextView) findViewById(R.id.fileName);
        //fileNameTextView.setText("Fichier à transmettre");

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setScaleY(3f);
        progressBar.setProgress(65);
        fileToSend = (File) getIntent().getExtras().get("fileToSend");


    }

    @Override
    protected void onStart(){
        super.onStart();

        Thread uploadThread = new Thread() {

            @Override
            public void run() {
                int size = (int) fileToSend.length();
                byte[] bytes = new byte[size];
                try {
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(fileToSend));
                    buf.read(bytes, 0, bytes.length);
                    buf.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BluetoothService.write(bytes);
                BluetoothService.close();
            }
        };
        uploadThread.start();


    }
}
