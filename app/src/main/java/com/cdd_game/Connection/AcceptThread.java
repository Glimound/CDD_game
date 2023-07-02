package com.cdd_game.Connection;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
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
    private HashMap<String, ConnectedThread> mmConnectedThreads;  // TODO:不是连接池 待实现连接池


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
        ConnectedThread thread = new ConnectedThread(mmSocket, mmHandler);
        thread.start();
        if (mmConnectedThreads.size() < 7) {    // 蓝牙api限制最大同时连接数为7
            mmConnectedThreads.put(mmSocket.getRemoteDevice().getAddress(), thread);    // 是否可以用socket池？而非线程池？
            mmBluetooth.setConnectedThreadsOfServer(mmConnectedThreads);
            mmHandler.sendEmptyMessage(Bluetooth.GOT_A_CLIENT);
        } else {
            Log.e("Bluetooth", "Concurrent connections reach limit.");
        }
    }
}
