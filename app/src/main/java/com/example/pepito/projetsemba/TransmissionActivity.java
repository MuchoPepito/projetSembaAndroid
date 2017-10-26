package com.example.pepito.projetsemba;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    private ProgressBar progressBar;
    private TextView fileNameTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmission);

        //TextView fileNameTextView = (TextView) findViewById(R.id.fileName);
        //fileNameTextView.setText("Fichier à transmettre");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        fileToSend = (File) getIntent().getExtras().get("fileToSend");
        fileNameTextView = (TextView) findViewById(R.id.fileNameToTransmit);


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
                Message msg = handler.obtainMessage();
                msg.what = TRANSFER_DONE;
                handler.sendMessage(msg);

            }
        };
        uploadThread.start();


    }

    private final int TRANSFER_DONE = 1;

    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==TRANSFER_DONE){
                fileNameTextView.setText("Fichier transmis.");
                progressBar.setVisibility(View.GONE);
            }
            super.handleMessage(msg);
        }
    };

}
