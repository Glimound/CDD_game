package com.cdd_game.Connection;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class AcceptThread extends Thread {
    private static final String NAME = "BluetoothClass";
    private static final UUID BLUETOOTH_UUID = UUID.fromString(Bluetooth.getBluetoothUUID());
    private final BluetoothServerSocket mmServerSocket;
    private final Bluetooth mmBluetooth;
    private final Handler mmHandler;
    private ConnectedThread mmConnectedThread;  // TODO:不是连接池 待实现连接池


    @SuppressLint("MissingPermission")
    public AcceptThread(Bluetooth bluetooth, Handler handler) {
        BluetoothServerSocket tmp = null;
        mmBluetooth = bluetooth;
        mmHandler = handler;

        try {
            tmp = mmBluetooth.getBluetoothAdapter().listenUsingRfcommWithServiceRecord(NAME, BLUETOOTH_UUID);
            Log.d("Bluetooth", "Server: Get server socket success.");
        } catch (IOException e) {
            Log.e("Bluetooth", "Server: Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // 持续监听
        while (true) {
            try {
                socket = mmServerSocket.accept();
                Log.d("Bluetooth", "Server: Get socket of connect device success.");
            } catch (IOException e) {
                Log.e("Bluetooth", "Server: Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                manageConnectedSocket(socket);  // 连接成功，执行操作
                socket = null;  //设为null，等待下一个连接
            }
        }
    }

    public void cancel() {
        try {
            mmServerSocket.close();
            Log.d("Bluetooth", "Server: Server socket canceled.");
        } catch (IOException e) {
            Log.e("Bluetooth", "Server: Could not close the connect socket", e);
        }
    }

    public void manageConnectedSocket(BluetoothSocket mmSocket) {
        mmConnectedThread = new ConnectedThread(mmSocket, mmHandler);
        mmConnectedThread.start();
        mmHandler.sendEmptyMessage(MessageType.GOT_A_CLIENT.ordinal());
    }
}
