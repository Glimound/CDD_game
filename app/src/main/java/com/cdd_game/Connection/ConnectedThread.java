package com.cdd_game.Connection;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cdd_game.Message.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
        mmBuffer = new byte[8192];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int numBytes = 0; // bytes returned from read()

        // 持续监听
        while (true) {
            try {
                // 从InputStream中读取字节流，存至mmBuffer中；未读取完时，写入
                while ((numBytes = mmInStream.read(mmBuffer)) > -1) {
                    baos.write(mmBuffer, 0, numBytes);
                    baos.flush();
                    Thread.sleep(200);
                    if (mmInStream.available() == 0)
                        break;
                }

                // 将读取的字节流反序列化为json，再由json生成对象
                String json = "";
                json = baos.toString("UTF-8").trim();
                baos.reset();
                JsonReader reader = new JsonReader(new StringReader(json));
                reader.setLenient(true);
                MessageSchema msg = typeCasting(reader, json);

                // 将收到的消息对象发回主线程
                Message readMsg = mmHandler.obtainMessage(Bluetooth.MESSAGE_READ, msg);
                mmHandler.sendMessage(readMsg);

            } catch (IOException e) {
                Log.e("Bluetooth", "Input stream was disconnected", e);
                break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void write(MessageSchema msg) {
        try {
            // 将对象序列化为json，再由json生成字节流
            String json = new Gson().toJson(msg);
            try {
                byte[] bytes = json.getBytes("UTF-8");
                mmOutStream.write(bytes);
            } catch (UnsupportedEncodingException e) {
                Log.e("Bluetooth", "Serialize failed", e);
            }

            // 将发送成功的消息发回主线程
            mmHandler.sendEmptyMessage(Bluetooth.MESSAGE_WRITTEN);

        } catch (IOException e) {
            Log.e("Bluetooth", "Error occurred when sending data", e);

            // 将发送失败的消息发回主线程
            mmHandler.sendEmptyMessage(Bluetooth.MESSAGE_SENT_FAILED);
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("Bluetooth", "Could not close the connect socket", e);
        }
    }

    private MessageSchema typeCasting(JsonReader reader, String json) {
        String behaviourType;
        try {
            JSONObject jsonObject = new JSONObject(json);
            behaviourType = (String) jsonObject.get("behaviour");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        switch (behaviourType) {
            case "PLAY_CARD":
                return new Gson().fromJson(reader, MsgPlayCard.class);
            case "NEXT_TURN":
                return new Gson().fromJson(reader, MsgNextTurn.class);
            case "PLAYER_JOINED":
                return new Gson().fromJson(reader, MsgPlayerJoined.class);
            case "SHAKE_HAND":
                return new Gson().fromJson(reader, MsgShakeHands.class);
            case "READY":
                return new Gson().fromJson(reader, MsgReady.class);
            case "GAME_START":
                return new Gson().fromJson(reader, MsgGameStart.class);
            case "GAME_END":
                return new Gson().fromJson(reader, MsgGameEnd.class);
        }
        return null;
    }
}