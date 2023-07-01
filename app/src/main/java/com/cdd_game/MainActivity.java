package com.cdd_game;

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

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                game();
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

    private void game(){

        final boolean[] isCardLifted = {false}; // 用于跟踪牌的状态，默认为未上升
        ImageButton imageButton3=(ImageButton) findViewById(R.id.imageButton3);

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
            String name="club_2";
            imageButton.setImageResource(R.drawable.club_2);
            imageButton.setBackground(new ColorDrawable(Color.TRANSPARENT));
            LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams)imageButton.getLayoutParams();
            if(i>0){
                layoutParams.leftMargin = -overlap; // 设置水平偏移量
            }
            layoutParams.gravity= Gravity.BOTTOM;
            imageButton.setLayoutParams(layoutParams);
            LinearLayout1.addView(imageButton);

            //final boolean[] isCardLifted = {false}; // 用于跟踪牌的状态，默认为未上升
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
                targetLayout.removeAllViews();
                for(int i=0;i<LinearLayout1.getChildCount();i++){
                    ImageButton child=(ImageButton) LinearLayout1.getChildAt(i);
                    if(child.getTranslationY()!=0){
                        LinearLayout1.removeView(child);
                        targetLayout.addView(child);
                        i--;
                    }
                }
            }
        });

    }
}