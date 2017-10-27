package com.example.pepito.projetsemba;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

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
        OutputStream outputStream;
        try {
            outputStream = bluetoothSocket.getOutputStream();
            if(outputStream == null)
                return;
            outputStream.write(buffer);
        } catch (IOException e) {

        }

        //signaler fin du message
    }

    public static boolean isConnected(){
        if(bluetoothSocket != null)
            return bluetoothSocket.isConnected();
        return false;
    }

    public static void close() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
