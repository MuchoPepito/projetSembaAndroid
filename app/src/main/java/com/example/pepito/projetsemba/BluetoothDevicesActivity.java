package com.example.pepito.projetsemba;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BluetoothDevicesActivity extends AppCompatActivity {
    final String tag = MainActivity.class.getName();
    protected List<String> dnList;
    protected Map<String, BluetoothDevice> devices;
    protected ListView dnListView;
    protected ArrayAdapter<String> dnArrayAdapter;

    protected BluetoothAdapter bTAdapter;


    public final static int REQUEST_BLUETOOTH = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_devices);

        devices = new HashMap<>();

        dnList = new ArrayList<>();

        dnListView = (ListView) findViewById(R.id.bluetoothDevices);
        dnArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dnList);
        dnListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        dnListView.setAdapter(dnArrayAdapter);


        bTAdapter = BluetoothAdapter.getDefaultAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        if (bTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if (!bTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }

        for(BluetoothDevice btDevice : bTAdapter.getBondedDevices()) {
            devices.put(btDevice.getName(), btDevice);
            dnList.add(btDevice.getName());
        }
        /*IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        dnArrayAdapter.clear();
        registerReceiver(bReceiver, filter);
        bTAdapter.startDiscovery();

        dnListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String deviceName = dnList.get(i);
                BluetoothDevice chosenDevice = devices.get(deviceName);
                /*ConnectThread connectThread = new ConnectThread(chosenDevice);
                connectThread.run();

            }
        });*/

    }


    /*private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getName() != null) {
                    devices.put(device.getName(), device);
                    dnList.add(device.getName());
                    dnArrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private void manageMyConnectedSocket(BluetoothSocket socket){
        Log.d(tag, "socketokok");
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.
            } catch (IOException e) {
                Log.e(tag, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bTAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(tag, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            manageMyConnectedSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(tag, "Could not close the client socket", e);
            }
        }
    }*/
}
