package com.cdd_game;

import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.cdd_game.Message.BehaviourType;
import com.cdd_game.Connection.Bluetooth;
import com.cdd_game.Connection.ConnectAdapter;
import com.cdd_game.Message.MessageSchema;
import com.cdd_game.Game.GameRoom;
import com.cdd_game.Message.MsgShakeHands;
import com.cdd_game.Player.Player;

import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public ConnectAdapter connector;
    public Player player = null;
    private GameRoom gameRoom = null;
    private Toast toast = null;
    public State state;
    private HashMap<Player, BluetoothSocket> socketMapping;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Bluetooth.CONNECTED_TO_SERVER: // 客户端连接上服务器，触发该分支，进入房间
                    connector.stopScanning();
                    connector.getConnectThread().cancel();
                    MessageSchema newMsg = new MsgShakeHands(Calendar.getInstance().getTimeInMillis(),
                            player.getDeviceID(), player.getNickName(), null, null);
                    connector.getConnectedThreadOfClient().write(newMsg);
                    // TODO: 在此处切换ui，进入游戏房间等待界面
                    break;
                case Bluetooth.GOT_A_CLIENT:    // 服务器连接上客户端

                    break;
                case Bluetooth.MESSAGE_READ:    // 接收到socket传来的msg

                    break;
            }
        }
    };

    private BroadcastReceiver bluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * BluetoothAdapter.STATE_OFF：关闭；BluetoothAdapter.STATE_ON：开启
             * BluetoothAdapter.STATE_TURNING_OFF：正在关闭；BluetoothAdapter.STATE_TURNING_ON：正在开启...
             */
            int bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
        }
    };

    private BroadcastReceiver bluetoothActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (state) {
                case SERVER_WAITING:
                    if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) { // 主机可发现性发生变化
                        int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                        if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE)
                            connector.enableVisibility(MainActivity.this);// discoverable时间到，重新打开
                    }
                    break;
                case CLIENT_SCANNING_GAME_ROOM:
                    if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) { // 开始扫描
                        connector.getDevices().clear(); // 清空已找到的devices
                    }
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {  // 发现了新设备
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        connector.getDevices().add(device);
                        @SuppressLint("MissingPermission") String deviceName = device.getName();
                        String deviceID = device.getAddress();

                        // 广播，修改ui（自定义intent）
                    }
                    break;
            }
        }
    };

    public ActivityResultLauncher<Intent> startBlueTooth =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK)
                    Log.d("Bluetooth", "Successful to open bluetooth");
                else
                    Log.d("Bluetooth", "Failed to open bluetooth");
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        IntentFilter bluetoothFilter = new IntentFilter();
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        bluetoothFilter.addAction(BluetoothDevice.ACTION_FOUND);
        bluetoothFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        registerReceiver(bluetoothActionReceiver, bluetoothFilter);

        registerReceiver(bluetoothStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

        connector = new Bluetooth();
        connector.initialize(this, handler);

        idle();
    }

    private void idle(){
        Button button1=(Button)this.findViewById(R.id.button1);//创建房间
        Button button2=(Button)this.findViewById(R.id.button2);  //加入房间

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setContentView(R.layout.room_setting);
                settingRoom();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.room_joining);
                searchingRoom();
            }
        });
    }

    private void settingRoom(){
        ImageButton imageButton= (ImageButton) this.findViewById(R.id.imageButton);
        Spinner spinner=(Spinner) findViewById(R.id.rule_dropdown);
        spinner.setSelection(0);
        EditText id=(EditText) findViewById(R.id.id);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * TODO: position为0是 规则设置为南方规则，为1时，设置为北方规则未开发（默认设置为南方规则）
                 */
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String nickName=s.toString();
                /**
                 * TODO:将用户名（此处为主机玩家）设置为id
                 */
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                idle();
            }
        });
    }

    private void searchingRoom(){
        ImageButton imageButton= (ImageButton) this.findViewById(R.id.imageButton_exit);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                idle();
            }
        });
    }

    public void showToast(String text) {
        if (toast == null)
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        else
            toast.setText(text);
        toast.show();
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED &&
                    this.checkSelfPermission(Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED &&
                    this.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_ADVERTISE,
                        Manifest.permission.BLUETOOTH_CONNECT}, 2);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {

        } else if (requestCode == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

        } else {
            checkPermission();
        }
    }
}