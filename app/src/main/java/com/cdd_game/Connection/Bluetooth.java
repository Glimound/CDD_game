package com.cdd_game.Connection;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;


public class Bluetooth {
    private BluetoothAdapter bluetoothAdapter;

    public Bluetooth() {

    }

    public void initalize(Context context) throws Exception {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            throw new Exception("Device bluetooth hardware unsupported");
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

//            context.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//                Intent data = result.getData();
//                int code = result.getResultCode();
//            });
        }
    }

    public void startScanning() {

    }

    public void stopScanning() {

    }

    public void startAdvertising() {

    }

    public void stopAdvertising() {

    }

    public boolean connectToRoom() {
        return true;
    }

    public void closeBluetooth() {

    }

}
