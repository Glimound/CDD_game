package com.cdd_game.Connection;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private byte[] mmBuffer;
    private final Handler mmHandler;

    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        mmSocket = socket;
        mmHandler = handler;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e("Bluetooth", "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e("Bluetooth", "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

    }

    public void run() {
        mmBuffer = new byte[1024];
        int numBytes; // bytes returned from read()

        // 持续监听
        while (true) {
            try {
                // 从InputStream中读取字节流，存至mmBuffer中
                numBytes = mmInStream.read(mmBuffer);

                // 将读取的字节流反序列化为json，再由json生成对象
                String json = "";
                try {
                    json = new String(mmBuffer, "UTF-8").trim();
                } catch (UnsupportedEncodingException e) {
                    Log.e("Bluetooth", "Deserialize failed", e);
                }
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.setLenient(true);
                MessageSchema msg = new Gson().fromJson(reader, MessageSchema.class);

                // 将收到的消息对象发回主线程
                Message readMsg = mmHandler.obtainMessage(MessageType.MESSAGE_READ.ordinal(), msg);
                mmHandler.sendMessage(readMsg);

            } catch (IOException e) {
                Log.e("Bluetooth", "Input stream was disconnected", e);
                break;
            }
        }
    }

    public void write(MessageSchema msg) {
        try {
            // 将对象序列化为json，再由json生成字节流
            String json = new Gson().toJson(msg);
            try {
                mmOutStream.write(json.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e("Bluetooth", "Serialize failed", e);
            }

            // 将发送成功的消息发回主线程
            mmHandler.sendEmptyMessage(MessageType.MESSAGE_WRITE.ordinal());

        } catch (IOException e) {
            Log.e("Bluetooth", "Error occurred when sending data", e);

            // 将发送失败的消息发回主线程
            mmHandler.sendEmptyMessage(MessageType.MESSAGE_SENT_FAILED.ordinal());
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("Bluetooth", "Could not close the connect socket", e);
        }
    }
}