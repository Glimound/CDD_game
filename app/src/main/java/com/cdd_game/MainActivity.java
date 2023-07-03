package com.cdd_game;

import androidx.annotation.NonNull;
import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cdd_game.Game.Game;
import com.cdd_game.Game.GameRoom;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.cdd_game.Message.BehaviourType;
import com.cdd_game.Connection.Bluetooth;
import com.cdd_game.Connection.ConnectAdapter;
import com.cdd_game.Message.MessageParser;
import com.cdd_game.Message.MessageSchema;
import com.cdd_game.Game.GameRoom;
import com.cdd_game.Message.MsgShakeHands;
import com.cdd_game.Player.Player;
import com.cdd_game.Rule.NormalRule;
import com.cdd_game.Rule.Rule;

import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public ConnectAdapter connector;
    public Player player = null;
    private GameRoom gameRoom = null;
    private Toast toast = null;
    public State state;
    private MessageParser messageParser = new MessageParser(this);
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
                    // TODO: 在此处切换ui，进入游戏房间等待界面（应为握手后）
                    break;
                case Bluetooth.GOT_A_CLIENT:    // 服务器连接上客户端

                    break;
                case Bluetooth.MESSAGE_READ:    // 接收到socket传来的msg
                    messageParser.parseMessage((MessageSchema) msg.obj);
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
        state = State.INIT;

        // Hide the status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        idle();
    }
    private FuncOfActivity func=new FuncOfActivity();

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
        state = State.SERVER_SETTING;
        ImageButton imageButton= (ImageButton) this.findViewById(R.id.imageButton);
        Button button=(Button)this.findViewById(R.id.button);
        Spinner spinner=(Spinner) findViewById(R.id.rule_dropdown);
        spinner.setSelection(0);
        EditText id=(EditText) findViewById(R.id.id);

        Rule[] rule = {new NormalRule()};
        String[] name = {"未命名"};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * position为0是 规则设置为南方规则，为1时，设置为北方规则未开发（默认设置为南方规则）
                 */
                if (position == 0) {
                    rule[0] = new NormalRule();
                }
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
                 * 将用户名（此处为主机玩家）设置为id
                 */
                name[0] = nickName;
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                idle();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 初始化游戏房间
                 */
                player = new Player(connector.getBluetoothAdapter().getAddress(), name[0]);
                connector.createRoom(MainActivity.this);
                ArrayList<Player> players = new ArrayList<>();
                GameRoom.createGameRoom(rule[0], 4, null, players);
                GameRoom.getGameRoomInstance().addPlayer(player);

                state=State.SERVER_WAITING;
                setContentView(R.layout.waiting1);
                waiting();
            }
        });
    }

    private void searchingRoom(){
        state=State.CLIENT_SCANNING_GAME_ROOM;
        ListView listView=this.findViewById(R.id.bt_device_list);
        ArrayList<String>itemList=new ArrayList<>();
        itemList.add("no1");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 *  position为点击项的索引
                 */
                state=State.CLIENT_WAITING;
                setContentView(R.layout.waiting1);
                waiting();
            }
        });
        ImageButton imageButton= (ImageButton) this.findViewById(R.id.imageButton_exit);
        EditText id=(EditText) findViewById(R.id.ClientId);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                idle();
            }
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
                 * TODO:将用户名（此处为加入房间玩家）设置为id
                 */
            }
        });
    }

    private void waiting(){
        Button button=this.findViewById(R.id.readyOrStar);
        /**
         * TODO:根据状态的不同设置按钮有不同的文本以及不同的动作
         */
        if(state==State.CLIENT_WAITING){
            button.setText("准备");
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    state=State.CLIENT_READY;
                    button.setText("等待游戏开始");
                }
            });
        }else if(state==State.SERVER_WAITING){
            button.setText("开始");
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    state=State.SERVER_PLAYING;
                    setContentView(R.layout.game_ui);
                    game();
                }
            });
        }
    }
    private void game(){

        HashMap<Integer,String>imageMap=new HashMap<>();

        ImageButton imageButton3=(ImageButton) findViewById(R.id.imageButton3);
        ImageView imageView2=(ImageView) findViewById(R.id.cards2);
        //player1，玩家自身
        LinearLayout LinearLayout1=(LinearLayout) findViewById(R.id.layout_player1);
        LinearLayout targetLayout=(LinearLayout) findViewById(R.id.Target_ui);
        int overlap=45;
        int liftDistance=40;

        for(int i=0;i<13;i++){
            ImageButton imageButton=new ImageButton(this);
            imageButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            /**
             * TODO:通过循环将卡牌放入布局中，如下面两行所示
             */
            String suit="CLUB";
            int resId=func.getDrawableId(this,suit.toLowerCase(),"3");
            String name=getResources().getResourceName(resId);
            imageMap.put(i,name);
            imageButton.setImageResource(resId);



            imageButton.setBackground(new ColorDrawable(Color.TRANSPARENT));
            LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams)imageButton.getLayoutParams();
            if(i>0){
                layoutParams.leftMargin = -overlap; // 设置水平偏移量
            }
            layoutParams.gravity= Gravity.BOTTOM;
            imageButton.setLayoutParams(layoutParams);
            LinearLayout1.addView(imageButton);

            final boolean[] isCardLifted = {false}; // 用于跟踪牌的状态，默认为未上升
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 创建上升动画
                    if(!isCardLifted[0]){
                        ObjectAnimator liftAnimator = ObjectAnimator.ofFloat(
                                imageButton, "translationY", 0, -liftDistance);
                        liftAnimator.setDuration(300); // 设置动画持续时间为300毫秒
                        liftAnimator.start();
                        isCardLifted[0]=true;
                    }
                    else{
                        // 创建下降动画
                        ObjectAnimator dropAnimator = ObjectAnimator.ofFloat(
                                imageButton, "translationY", -liftDistance, 0);
                        dropAnimator.setDuration(300); // 设置动画持续时间为300毫秒

                        dropAnimator.start();
                        isCardLifted[0]=false;
                    }

                }
            });
        }
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 还未测试
                 */
                targetLayout.removeAllViews();
                int num=0;
                int num1=0;
                int count=LinearLayout1.getChildCount();

                HashMap<Integer,String>tempMap=new HashMap<>();

                for(int i=0;i<LinearLayout1.getChildCount();i++){
                    ImageButton child=(ImageButton) LinearLayout1.getChildAt(i);
                    if(child.getTranslationY()!=0){
                        /**
                         * 通过for循环玩家所出的牌
                         */
                        String tempCardName=imageMap.get(num1);
                        int index=tempCardName.indexOf("_");
                        String cardSuit=tempCardName.substring(0,index).toUpperCase();
                        String cardRank=tempCardName.substring(index+1,tempCardName.length()).toUpperCase();


                        LinearLayout1.removeView(child);
                        targetLayout.addView(child);
                        i--;
                        count--;


                    }else{
                        tempMap.put(num,imageMap.get(num1));
                        num++;
                    }
                    num1++;
                }
                for(int i=0;i<count;i++){
                    ImageButton child=(ImageButton) LinearLayout1.getChildAt(i);
                    LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams)child.getLayoutParams();
                    if(i>0){
                        layoutParams.leftMargin = -overlap; // 设置水平偏移量
                    }else
                    {
                        layoutParams.leftMargin = 0;
                    }
                }

                if(tempMap.size()==count){
                    imageMap.putAll(tempMap);
                }
                LinearLayout1.setGravity(View.TEXT_ALIGNMENT_CENTER);

                /**
                 * 其余玩家牌变少，仅为测试用，后续需修改
                 * imageView2.setImageResource(func.getDrawableId(MainActivity.this,num));其中num为剩下牌的数量
                 */
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