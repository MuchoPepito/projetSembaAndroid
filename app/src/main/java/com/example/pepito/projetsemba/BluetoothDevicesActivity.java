package com.example.pepito.projetsemba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BluetoothDevicesActivity extends AppCompatActivity {

    final String tag = MainActivity.class.getName();
    protected List<String> dnList;
    protected ListView pdnListView;
    protected ArrayAdapter<String> dnArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_devices);

        dnList = new ArrayList<>();
        dnList.add("periph 1");
        dnList.add("periph 2");
        dnList.add("periph 3");
        dnList.add("periph 4");

        pdnListView = (ListView) findViewById(R.id.bluetoothDevices);
        dnArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dnList);
        pdnListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        pdnListView.setAdapter(dnArrayAdapter);
        
    }
}
