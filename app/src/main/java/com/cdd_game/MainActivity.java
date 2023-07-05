package com.cdd_game;

import androidx.annotation.NonNull;
import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cdd_game.Card.Card;
import com.cdd_game.Card.CardGroup;
import com.cdd_game.Card.CardGroupType;
import com.cdd_game.Card.CardPoolFactory;
import com.cdd_game.Game.Game;
import com.cdd_game.Chat.ChatAdapter;
import com.cdd_game.Chat.ChatData;
import com.cdd_game.Game.GameRoom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.cdd_game.Connection.Bluetooth;
import com.cdd_game.Connection.ConnectAdapter;
import com.cdd_game.Message.*;
import com.cdd_game.Player.Player;
import com.cdd_game.Rule.NormalRule;
import com.cdd_game.Rule.Rule;

import java.util.Calendar;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public ConnectAdapter connector;
    /**
     * 存储自身的MAC地址和nickName
     */
    public Player player = null;
    private GameRoom gameRoom = null;
    private Toast toast = null;
    public State state;
    private MessageParser messageParser = new MessageParser(this);
    private HashMap<Player, BluetoothSocket> socketMapping;
    ArrayList<ChatData> chatList=new ArrayList<>();
    ChatAdapter chatAdapter;
    public String tmpMAC;
    List<String> itemList = Collections.synchronizedList(new ArrayList<>());
    //ArrayList<String>itemList=new ArrayList<>();
    ArrayAdapter<String> adapter;

    public ImageView imageA;
    public ImageView imageB;
    public ImageView imageC;
    public ImageView imageD;

    public ImageView imageA1;
    public ImageView imageB1;
    public ImageView imageC1;
    public ImageView imageD1;
    public ImageButton imageButton2;
    public ImageButton imageButton3;

    public ImageView imageF2;
    public ImageView imageF3;
    public ImageView imageF4;
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Bluetooth.CONNECTED_TO_SERVER: // 客户端连接上服务器，触发该分支，进入房间
                    //connector.getConnectThread().cancel();
                    MessageSchema newMsg = new MsgShakeHands(Calendar.getInstance().getTimeInMillis(),
                            player.getDeviceID(), player.getNickName(), null, null, 0, null);
                    connector.getConnectedThreadOfClient().write(newMsg);
                    break;

                case Bluetooth.GOT_A_CLIENT:    // 服务器连接上客户端
                    // 临时储存该客户端的MAC地址，握手时修改MAC-线程映射为nickName-线程映射
                    tmpMAC = (String) msg.obj;
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

                        // 修改ui
                        itemList.add(deviceName + " | " + deviceID);
                        adapter.notifyDataSetChanged();
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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
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
        state = State.INIT;
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
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name[0] = "未命名";
                setContentView(R.layout.activity_main);
                idle();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void searchingRoom(){
        state=State.CLIENT_SCANNING_GAME_ROOM;
        connector.startScanning();  // 开始扫描设备
        ListView listView=this.findViewById(R.id.bt_device_list);
        listView.setAdapter(adapter);

        String[] name = {"未命名"};

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 *  position为点击项的索引
                 */
                player = new Player(connector.getBluetoothAdapter().getAddress(), name[0]);
                connector.connectToRoom(position);
                // TODO: 进度条


//                state=State.CLIENT_WAITING;
//                setContentView(R.layout.waiting1);
//                waiting();
            }
        });

        ImageButton imageButton= (ImageButton) this.findViewById(R.id.imageButton_exit);
        EditText id=(EditText) findViewById(R.id.ClientId);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connector.getDevices().clear();
                itemList.clear();

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
                 * 将用户名（此处为加入房间玩家）设置为id
                 */
                name[0] = nickName;
            }
        });
    }

    public void waiting(){
        Button button=this.findViewById(R.id.readyOrStar);
        imageA=this.findViewById(R.id.a);
        imageB=this.findViewById(R.id.b);
        imageC=this.findViewById(R.id.c);
        imageD=this.findViewById(R.id.d);

        imageA1=this.findViewById(R.id.a1);
        imageB1=this.findViewById(R.id.b1);
        imageC1=this.findViewById(R.id.c1);
        imageD1=this.findViewById(R.id.d1);

        imageF2=this.findViewById(R.id.flag2);
        imageF3=this.findViewById(R.id.flag3);
        imageF4=this.findViewById(R.id.flag4);
        if(state==State.SERVER_WAITING){
            imageA.setVisibility(View.VISIBLE);
        }
        /**
         * 根据状态的不同设置按钮有不同的文本以及不同的动作
         */
        if(state==State.CLIENT_WAITING){
            button.setText("准备");
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    state=State.CLIENT_READY;
                    // 设置自身的准备状态，并发送消息至服务器
                    player.setReady(true);
                    GameRoom.getGameRoomInstance().getPlayerByNickName(player.getNickName()).setReady(true);
                    MessageSchema msg = new MsgReady(Calendar.getInstance().getTimeInMillis(),
                            player.getDeviceID(), player.getNickName());
                    connector.getConnectedThreadOfClient().write(msg);
                    imageA.setVisibility(View.VISIBLE);
                    // TODO: 屏蔽该button

                    button.setText("等待游戏开始");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }
            });
        }else if(state==State.SERVER_WAITING){
            button.setText("开始");
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int readyPlayerNum = 0;
                    if (!GameRoom.getGameRoomInstance().getPlayers().isEmpty()) {
                        for (Player player : GameRoom.getGameRoomInstance().getPlayers()) {
                            if (player.isReady())
                                readyPlayerNum++;
                        }
                    }

                    if (readyPlayerNum == GameRoom.getGameRoomInstance().getPlayerNumLimit() - 1) {
                        // 生成gameID
                        GameRoom gameRoom = GameRoom.getGameRoomInstance();
                        String gameID = UUID.randomUUID().toString();
                        // 新建游戏实例
                        gameRoom.createGame(gameID);
                        // 初始化游戏（发牌、设置出牌顺序）
                        Game game = Game.getGameInstance();
                        game.initialize(gameRoom.getWinner());  // 初始化后game内的players顺序与gameRoom内的players顺序不再相同
                        // 发送消息给所有其他玩家
                        MessageSchema msg = new MsgGameStart(Calendar.getInstance().getTimeInMillis(),
                                player.getDeviceID(), player.getNickName(), gameID, game.getPlayers());
                        for (Player player : gameRoom.getPlayers()) {
                            if (!player.getNickName().equals(player.getNickName())) {
                                connector.getConnectedThreadsOfServer().get(player.getNickName()).write(msg);
                                Log.d("Message", "Server sent start game message to " + player.getNickName() + ".");
                            }
                        }
                        state=State.SERVER_PLAYING;
                        setContentView(R.layout.game_ui);
                        game();
                    } else {
                        Log.d("Game", "Not all players are ready.");
                        readyPlayerNum = 0;
                    }
                }
            });
        }
    }

    /**
     * 其他玩家出牌（cards），更新ui
     * @param cards
     */
    public void showCardsUsed(String nickName, CardGroup cards){

        LinearLayout targetLayout=(LinearLayout) findViewById(R.id.Target_ui);
        ImageView imageView2=(ImageView) findViewById(R.id.cards2);
        ImageView imageView3=(ImageView) findViewById(R.id.cards3);
        ImageView imageView4=(ImageView) findViewById(R.id.cards4);
        targetLayout.removeAllViews();
        int overlap=45;
        for(int i=0;i< cards.size();i++){
            ImageButton imageButton=new ImageButton(this);
            imageButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            String suit=cards.getCards().get(i).getSuit().getName();
            String rank=cards.getCards().get(i).getRank().getName();
            int resId=func.getDrawableId(this,suit.toLowerCase(),rank);

            imageButton.setImageResource(resId);

            imageButton.setBackground(new ColorDrawable(Color.TRANSPARENT));
            LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams)imageButton.getLayoutParams();
            if(i>0){
                layoutParams.leftMargin = -overlap; // 设置水平偏移量
            }
            targetLayout.addView(imageButton);
        }
        Player curPlayer=GameRoom.getGameRoomInstance().getPlayerByNickName(nickName);
        int numOfCard=curPlayer.getOwnCards().size();
        int num=GameRoom.getGameRoomInstance().getPlayers().indexOf(curPlayer);
        int num1=GameRoom.getGameRoomInstance().getPlayers().indexOf(player);
        int offset=num-num1;
        switch(offset){
            case 1:
                imageView2.setImageResource(func.getDrawableId(MainActivity.this,numOfCard));
                break;
            case 2:
                imageView3.setImageResource(func.getDrawableId(MainActivity.this,numOfCard));
                break;
            case 3:
                imageView4.setImageResource(func.getDrawableId(MainActivity.this,numOfCard));
                break;
            case -1:
                imageView4.setImageResource(func.getDrawableId(MainActivity.this,numOfCard));
                break;
            case -2:
                imageView3.setImageResource(func.getDrawableId(MainActivity.this,numOfCard));
                break;
            case -3:
                imageView2.setImageResource(func.getDrawableId(MainActivity.this,numOfCard));
                break;
        }
    }

    public void game(){

        HashMap<Integer,String>imageMap=new HashMap<>();

        imageButton2=(ImageButton) findViewById(R.id.imageButton2);
        imageButton3=(ImageButton) findViewById(R.id.imageButton3);
        ImageView imageView2=(ImageView) findViewById(R.id.cards2);

        /**
         * 消息按钮，离开按钮，聊天框的布局文件
         *
          */
        ImageButton messageButton = findViewById(R.id.messageButton);
        ImageButton exitButton=findViewById(R.id.exit);
        Button sendButton=findViewById(R.id.send_button);
        LinearLayout dialogBox = findViewById(R.id.dialog_box);
        ListView listView=findViewById(R.id.chatContent);
        String inputMessage=findViewById(R.id.input_message).toString();
        listView.setAdapter(chatAdapter);
        // 设置点击监听器
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //显示聊天框
                    dialogBox.setVisibility(View.VISIBLE);
                    messageButton.setVisibility(View.GONE);
                }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox.setVisibility(View.GONE);
                messageButton.setVisibility(View.VISIBLE);
            }
        });
        //发送消息
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inputMessage.isEmpty()){
                    //TODO:将头像更换为对应头像
                    ChatData myChat=new ChatData(R.drawable.back,inputMessage,0);
                    receiveChat(myChat,listView);
                }
            }
        });


        //player1，玩家自身
        LinearLayout LinearLayout1=(LinearLayout) findViewById(R.id.layout_player1);
        LinearLayout targetLayout=(LinearLayout) findViewById(R.id.Target_ui);
        int overlap=45;
        int liftDistance=40;

        String nickNameOfPlayerToPlayCards = Game.getGameInstance().getPlayerToPlayCard().getNickName();
        if (!nickNameOfPlayerToPlayCards.equals(player.getNickName())) {
            // 若第一个出牌的人不为自己
            // TODO: 隐藏自己的出牌按钮和跳过按钮
        }
        // TODO: 显示轮到的玩家

        for(int i=0;i<13;i++){
            ImageButton imageButton=new ImageButton(this);
            imageButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            /**
             * 通过循环将卡牌放入布局中
             */
            Card card = Game.getGameInstance().getPlayerByNickName(player.getNickName()).getOwnCards().getCards().get(i);
            String suit = card.getSuit().getName().toLowerCase();
            String rank = card.getRank().getName();

            int resId=func.getDrawableId(this, suit, rank);
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
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 点击不要
                 */
                MessageSchema msg = new MsgNextTurn(Calendar.getInstance().getTimeInMillis(),
                        player.getDeviceID(), player.getNickName());
                if (state == State.CLIENT_PLAYING) {
                    connector.getConnectedThreadOfClient().write(msg);
                    Log.d("Message", "Client sent next turn message to server.");
                } else if (state == State.SERVER_PLAYING) {
                    for (Player player : Game.getGameInstance().getPlayers()) {
                        if (!player.getNickName().equals(player.getNickName())) {
                            connector.getConnectedThreadsOfServer().get(player.getNickName()).write(msg);
                            Log.d("Message", "Server sent next turn message to " + player.getNickName() + ".");
                        }
                    }
                }
            }
        });
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
                ArrayList<ImageButton> children=new ArrayList<>();
                HashMap<Integer,String>tempMap=new HashMap<>();

                // 创建空卡组
                CardGroup cardGroup;
                try {
                    cardGroup = new CardPoolFactory().createCardGroup(CardGroupType.UNKNOWN);
                } catch (Exception e) {
                    Log.e("Game", "Card group create failed.");
                    throw new RuntimeException(e);
                }

                Game game = Game.getGameInstance();
                for(int i=0;i<LinearLayout1.getChildCount();i++){
                    ImageButton child=(ImageButton) LinearLayout1.getChildAt(i);
                    if(child.getTranslationY()!=0){
                        /**
                         * 通过for循环玩家所出的牌
                         */
                        children.add(child);

                        String tempCardName=imageMap.get(num1);
                        int index=tempCardName.indexOf("_");
                        String cardSuit=tempCardName.substring(0,index).toUpperCase();
                        String cardRank=tempCardName.substring(index+1).toUpperCase();
                        Card card = game.getPlayerByNickName(player.getNickName()).getOwnCards()
                                .getCardBySuitAndRank(cardSuit, cardRank);
                        cardGroup.addCard(card);

                        //i--;
                        count--;


                    }else{
                        tempMap.put(num,imageMap.get(num1));
                        num++;
                    }
                    num1++;
                }

                boolean isValid = game.getRule().validate(cardGroup);
                boolean isBigger;
                if (game.getPreviousCards() == null)
                    isBigger = true;
                else
                    isBigger = game.getRule().compareToCards(game.getPreviousCards(), cardGroup);

                /**
                 * 如果牌型正确且比上家大
                 */
                if (isValid && isBigger) {
                    // 发送消息给服务器（客户端）
                    MessageSchema msg = new MsgPlayCard(Calendar.getInstance().getTimeInMillis(),
                            player.getDeviceID(), player.getNickName(), cardGroup);
                    if (state == State.CLIENT_PLAYING) {
                        connector.getConnectedThreadOfClient().write(msg);
                        Log.d("Message", "Client sent play card message to server." + "\n\tPlayer: "
                                + player.getNickName() + "\n\tCards: " + cardGroup.toString());
                    } else if (state == State.SERVER_PLAYING) {
                        for (Player player : Game.getGameInstance().getPlayers()) {
                            if (!player.getNickName().equals(player.getNickName())) {
                                connector.getConnectedThreadsOfServer().get(player.getNickName()).write(msg);
                                Log.d("Message", "Server sent play card message to "
                                        + player.getNickName() + ".\n\tPlayer: " + player.getNickName()
                                        + "\n\tCards: " + cardGroup.toString());
                            }
                        }
                    }

                    // 更新UI
                    for(int i=0;i< children.size();i++){
                        LinearLayout1.removeView(children.get(i));
                        children.get(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        targetLayout.addView(children.get(i));
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

                    game.getPlayerByNickName(player.getNickName()).getOwnCards().removeCards(cardGroup);
                    /** TODO: 更新UI中自己牌的张数
                     * 其余玩家牌变少，仅为测试用，后续需修改
                     * imageView2.setImageResource(func.getDrawableId(MainActivity.this,num));其中num为剩下牌的数量
                     */

                } else {
                    showToast("牌型不符合规则");
                    // TODO: 更新UI，显示牌型不符合规则
                }


                LinearLayout1.setGravity(View.TEXT_ALIGNMENT_CENTER);

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

    private void gameSettlement(){
            Button continue_button=(Button)this.findViewById(R.id.continue_button);//创建房间
            Button end_button=(Button)this.findViewById(R.id.end_button);  //加入房间

            continue_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    setContentView(R.layout.game_ui);
                    game();
                }
            });
            end_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setContentView(R.layout.activity_main);
                    idle();
                }
            });
    }
    public void receiveChat(ChatData chatData,ListView listView){
        //将新消息发送到数据源中
        chatList.add(chatData);
        //让适配器更新列表
        chatAdapter.notifyDataSetChanged();
        //滚动到最底部
       listView.smoothScrollToPosition(chatList.size() - 1);
    }
}