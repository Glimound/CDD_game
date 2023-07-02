package com.cdd_game;

import android.content.Context;

import java.lang.reflect.Field;

public class FuncOfActivity {
    FuncOfActivity(){}

    public static int getDrawableId(Context context, String suit,String rank) {

        try {
            int imageId = context.getResources().getIdentifier(suit+'_'+rank, "drawable", context.getPackageName());
            return imageId;
        } catch (Exception e) {
            return 0;
        }
    }


    public static int getDrawableId(Context context, int num) {

        try {
            int imageId = context.getResources().getIdentifier("back"+num, "drawable", context.getPackageName());
            return imageId;
        } catch (Exception e) {
            return 0;
        }
    }
}
