package com.example.pepito.projetsemba;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    final String tag = MainActivity.class.getName();
    protected List<String> fnList;
    protected ListView fnListView;
    protected ArrayAdapter<String> fnArrayAdapter;

    protected BluetoothSocket bluetoothSocket;

    protected static final int BLUETOOTH_SOCKET_REQUEST = 1;

    Button leftButton, rightButton, rmButton, leaveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);


        fnList = new ArrayList<>();

        //String path = "/storage/emulated/0/DCIM/Camera/"
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File pictureFolder = new File(sdCardRoot, Environment.DIRECTORY_DCIM + "/Camera/");
        File[] pictures = pictureFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.toLowerCase().endsWith("jpg");
            }
        });

        final Map<String, File> hmPicture = new HashMap<>();

        for(File picture : pictures)
            if(picture.isFile()) {
                fnList.add(picture.getName());
                hmPicture.put(picture.getName(), picture);
            }


        fnListView = (ListView) findViewById(R.id.listView);
        fnArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fnList);
        fnListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        fnListView.setAdapter(fnArrayAdapter);

        fnListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(BluetoothService.isConnected()) {
                    BluetoothService.write("uploadPhoto\0".getBytes());
                    Intent intent = new Intent(getApplicationContext(), TransmissionActivity.class);
                    String fileName = fnList.get(i);
                    File fileToSend = hmPicture.get(fileName);
                    intent.putExtra("fileToSend", fileToSend);

                    startActivity(intent);
                }else
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
            }
        });

        rightButton = (Button) findViewById(R.id.rightButton);
        leftButton = (Button) findViewById(R.id.leftButton);
        rmButton = (Button) findViewById(R.id.rmButton);
        leaveButton = (Button) findViewById(R.id.leaveButton);

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BluetoothService.isConnected()) {
                    BluetoothService.write("rightPhoto\0".getBytes());
                }else
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BluetoothService.isConnected()) {
                    BluetoothService.write("leftPhoto\0".getBytes());
                }else
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
            }
        });

        rmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BluetoothService.isConnected()) {
                    BluetoothService.write("removePhoto\0".getBytes());
                }else
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
            }
        });

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BluetoothService.isConnected()) {
                    BluetoothService.write("leaveApp\0".getBytes());
                    BluetoothService.close();
                }else
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, BluetoothDevicesActivity.class);
            startActivityForResult(intent, BLUETOOTH_SOCKET_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == BLUETOOTH_SOCKET_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String deviceName = data.getStringExtra("deviceName");
                Toast.makeText(this, "Connected to : " + deviceName,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
