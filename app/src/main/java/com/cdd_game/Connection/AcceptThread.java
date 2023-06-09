package com.cdd_game.Connection;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AcceptThread extends Thread {
    private static final String NAME = "BluetoothClass";
    private static final UUID BLUETOOTH_UUID = UUID.fromString(Bluetooth.getBluetoothUUID());
    private final BluetoothServerSocket mmServerSocket;
    private final Bluetooth mmBluetooth;
    private final Handler mmHandler;
    private ConnectedThread mmConnectedThread;


    @SuppressLint("MissingPermission")
    public AcceptThread(Bluetooth bluetooth, Handler handler) {
        BluetoothServerSocket tmp = null;
        mmBluetooth = bluetooth;
        mmHandler = handler;

        try {
            tmp = mmBluetooth.getBluetoothAdapter().listenUsingRfcommWithServiceRecord(NAME, BLUETOOTH_UUID);
            Log.d("Bluetooth", "Server: Get server socket success.");
        } catch (IOException e) {
            Log.e("Bluetooth", "Server: Socket's listen() method failed.", e);
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
                Log.e("Bluetooth", "Server: Socket's accept() method failed.", e);
                break;
            }

            if (socket != null) {
                Log.d("Bluetooth", "Server: Connected to client success.");
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
            Log.e("Bluetooth", "Server: Could not close the connect socket.", e);
        }
    }

    public void manageConnectedSocket(BluetoothSocket mmSocket) {
        mmConnectedThread = new ConnectedThread(mmSocket, mmHandler);
        if (mmBluetooth.getConnectedThreadsOfServer().size() < 7) {    // 蓝牙api限制最大同时连接数为7
            mmBluetooth.getConnectedThreadsOfServer().put(mmSocket.getRemoteDevice().getAddress(), mmConnectedThread);    // 是否可以用socket池？而非线程池？
            mmConnectedThread.start();
            Message msg = mmHandler.obtainMessage(Bluetooth.GOT_A_CLIENT, mmSocket.getRemoteDevice().getAddress());
            mmHandler.sendMessage(msg);
        } else {
            Log.e("Bluetooth", "Concurrent connections reach limit.");
        }
    }
}
