package com.cdd_game.Connection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cdd_game.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Bluetooth implements ConnectAdapter {
    public final static int MSG_ERROR = 0, CONNECTED_TO_SERVER = 1, GOT_A_CLIENT = 2,
            MESSAGE_READ = 3, MESSAGE_WRITTEN = 4, MESSAGE_SENT_FAILED = 5;

    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BluetoothDevice> devices; // TODO: 线程不安全
    private BluetoothServerSocket serverSocket;
    private BluetoothSocket clientSocket;
    private Handler handler;
    private static final String BLUETOOTH_UUID = "942F0AD5-F206-AD5E-D451-2DE5143647F3";

    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThreadOfClient;
    private HashMap<String, ConnectedThread> connectedThreadsOfServer;

    public Bluetooth() {
    }

    public void initialize(MainActivity activity, Handler handler) {
        if (this.bluetoothAdapter != null) {
            this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (this.bluetoothAdapter == null) {
                Log.e("Bluetooth", "Device bluetooth hardware unsupported.");
                activity.showToast("Device bluetooth hardware unsupported.");
            }
        }
        this.devices = new ArrayList<>();
        this.handler = handler;
        this.acceptThread = null;
        this.connectThread = null;
    }

    /**
     * 打开蓝牙
     * @param activity MainActivity
     */
    public void open(MainActivity activity) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startBlueTooth.launch(enableBtIntent);
        }
    }

    @SuppressLint("MissingPermission")
    public void close() {
        bluetoothAdapter.disable();
    }

    @SuppressLint("MissingPermission")
    public void enableVisibility(MainActivity activity) {
        Intent discoverableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableBtIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        activity.startActivity(discoverableBtIntent);
    }

    @SuppressLint("MissingPermission")
    public void disableVisibility(MainActivity activity) {
        Intent discoverableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableBtIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1);
        activity.startActivity(discoverableBtIntent);
    }

    @SuppressLint("MissingPermission")
    public void startScanning() {
        bluetoothAdapter.startDiscovery();
    }


    @SuppressLint("MissingPermission")
    public void stopScanning() {
        bluetoothAdapter.cancelDiscovery();
    }

    public void createRoom() {

    }

    public void connectToRoom(int index) {
        BluetoothDevice device = devices.get(index);

        // 连接至指定设备，创建connectThread，创建connectedThreadOfClient
        if (connectThread != null)
            connectThread.cancel();
        connectThread = new ConnectThread(device, this, handler);
        connectThread.start();
    }

    public ArrayList<BluetoothDevice> getDevices() {
        return devices;
    }

    public BluetoothServerSocket getServerSocket() {
        return serverSocket;
    }

    public BluetoothSocket getClientSocket() {
        return clientSocket;
    }

    public void setServerSocket(BluetoothServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setClientSocket(BluetoothSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public static String getBluetoothUUID() {
        return BLUETOOTH_UUID;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public AcceptThread getAcceptThread() {
        return acceptThread;
    }

    public void setAcceptThread(AcceptThread acceptThread) {
        this.acceptThread = acceptThread;
    }

    public ConnectThread getConnectThread() {
        return connectThread;
    }

    public void setConnectThread(ConnectThread connectThread) {
        this.connectThread = connectThread;
    }

    public ConnectedThread getConnectedThreadOfClient() {
        return connectedThreadOfClient;
    }

    public void setConnectedThreadOfClient(ConnectedThread connectedThreadOfClient) {
        this.connectedThreadOfClient = connectedThreadOfClient;
    }

    public HashMap<String, ConnectedThread> getConnectedThreadsOfServer() {
        return connectedThreadsOfServer;
    }

    public void setConnectedThreadsOfServer(HashMap<String, ConnectedThread> connectedThreadsOfServer) {
        this.connectedThreadsOfServer = connectedThreadsOfServer;
    }
}
