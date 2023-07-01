package com.cdd_game.Connection;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private static final UUID BLUETOOTH_UUID = UUID.fromString(Bluetooth.getBluetoothUUID());
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final Handler mmHandler;
    private final Bluetooth mmBluetooth;
    private ConnectedThread mmConnectedThread;

    @SuppressLint("MissingPermission")
    public ConnectThread(BluetoothDevice device, Bluetooth bluetooth, Handler handler) {
        BluetoothSocket tmp = null;
        mmBluetooth = bluetooth;
        mmDevice = device;
        mmHandler = handler;

        try {
            tmp = device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
            Log.d("Bluetooth", "Client: Get socket success.");
        } catch (IOException e) {
            Log.e("Bluetooth", "Client: Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    @SuppressLint("MissingPermission")
    public void run() {
        mmBluetooth.stopScanning();   // 关闭扫描

        try {
            // 阻塞直到返回
            mmSocket.connect();
        } catch (IOException connectException) {
            cancel();
            return;
        }

        manageConnectedSocket(mmSocket);  // 连接成功，执行操作
    }

    public void cancel() {
        try {
            mmSocket.close();
            Log.d("Bluetooth", "Client: Client socket closed.");
        } catch (IOException e) {
            Log.e("Bluetooth", "Could not close the client socket", e);
        }
    }

    public void manageConnectedSocket(BluetoothSocket mmSocket) {
        mmConnectedThread = new ConnectedThread(mmSocket, mmHandler);
        mmConnectedThread.start();
        mmHandler.sendEmptyMessage(MessageType.MSG_CONNECTED_TO_SERVER.ordinal());
        // TODO: 发送初始化信息：player name等等 写在main activity中
    }

}
