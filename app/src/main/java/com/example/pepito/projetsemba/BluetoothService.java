package com.example.pepito.projetsemba;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Pepito on 25/10/2017.
 */

public final class BluetoothService {

    private static BluetoothSocket bluetoothSocket;

    public static void setSocket(BluetoothSocket socket){
        bluetoothSocket = socket;
    }

    /**
     * Write to the connected OutStream.
     * @param buffer  The bytes to write
     */
    public static void write(byte[] buffer) {
        if(bluetoothSocket == null)
            return;
        try {
            bluetoothSocket.getOutputStream().write(buffer);

        } catch (IOException e) {

        }
    }

}
