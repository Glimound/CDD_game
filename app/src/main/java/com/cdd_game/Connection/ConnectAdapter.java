package com.cdd_game.Connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import com.cdd_game.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ConnectAdapter {
    AcceptThread acceptThread = null;
    ConnectThread connectThread = null;
    ConnectedThread connectedThreadOfClient = null;

    void initialize(MainActivity activity, Handler handler);

    List<BluetoothDevice> getDevices();

    void enableVisibility(MainActivity activity);

    void disableVisibility(MainActivity activity);

    void startScanning();

    void stopScanning();

    void createRoom(MainActivity activity);

    void connectToRoom(int index);

    BluetoothAdapter getBluetoothAdapter();

    BluetoothServerSocket getServerSocket();

    BluetoothSocket getClientSocket();

    AcceptThread getAcceptThread();

    ConnectThread getConnectThread();

    ConnectedThread getConnectedThreadOfClient();

    HashMap<String, ConnectedThread> getConnectedThreadsOfServer();

    void setServerSocket(BluetoothServerSocket serverSocket);

    void setClientSocket(BluetoothSocket clientSocket);

    void setAcceptThread(AcceptThread acceptThread);

    void setConnectThread(ConnectThread connectThread);

    void setConnectedThreadOfClient(ConnectedThread connectedThreadOfClient);

    void setConnectedThreadsOfServer(HashMap<String, ConnectedThread> connectedThreadsOfServer);

}
