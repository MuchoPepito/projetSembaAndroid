package com.example.pepito.projetsemba;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothDevicesActivity extends AppCompatActivity {

    final String tag = MainActivity.class.getName();
    protected List<String> dnList;
    protected ListView dnListView;
    protected ArrayAdapter<String> dnArrayAdapter;

    protected BluetoothAdapter bTAdapter;


    public final static int REQUEST_BLUETOOTH = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_devices);

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

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        dnArrayAdapter.clear();
        registerReceiver(bReceiver, filter);
        bTAdapter.startDiscovery();

    }


    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                dnList.add(device.getName());
                dnArrayAdapter.notifyDataSetChanged();
            }
        }
    };
}
