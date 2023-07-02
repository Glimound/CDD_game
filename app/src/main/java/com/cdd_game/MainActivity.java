package com.cdd_game;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cdd_game.Game.GameRoom;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public GameRoom gameRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        ImageButton imageButton= (ImageButton) this.findViewById(R.id.imageButton);
        Button button=(Button)this.findViewById(R.id.button);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.game_ui);
                /**
                 * TODO:初始化游戏房间
                 */

                game();
            }
        });
    }

    private void searchingRoom(){
        ImageButton imageButton= (ImageButton) this.findViewById(R.id.imageButton_exit);
        EditText id=(EditText) findViewById(R.id.id);
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

    private void game(){

        HashMap<Integer,String>imageMap=new HashMap<>();

        ImageButton imageButton3=(ImageButton) findViewById(R.id.imageButton3);
        ImageView imageView2=(ImageView) findViewById(R.id.cards2);
        //player1，玩家自身
        LinearLayout LinearLayout1=(LinearLayout) findViewById(R.id.layout_player1);
        LinearLayout targetLayout=(LinearLayout) findViewById(R.id.Target_ui);
        int overlap=40;
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
                if(tempMap.size()==count){
                    imageMap.putAll(tempMap);
                }
                LinearLayout1.setGravity(View.TEXT_ALIGNMENT_CENTER);

                /**
                 * 其余玩家牌变少，仅为测试用，后续需修改
                 *                     imageView2.setImageResource(func.getDrawableId(MainActivity.this,num));其中num为剩下牌的数量
                 */
            }

        });

    }
}