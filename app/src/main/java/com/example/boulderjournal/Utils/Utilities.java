package com.example.boulderjournal.Utils;

import android.content.Context;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;

import com.example.boulderjournal.R;

public class Utilities {

    private Context mContext;

    public static int getColor(String color, Context context) {
        String newColor = color.toLowerCase();

        int colourInt;
        if(newColor.equals("pink")){
            colourInt = ContextCompat.getColor(context, R.color.routePink);
        }else if(newColor.equals("blue")){
            colourInt = ContextCompat.getColor(context, R.color.routeBlue);
        }else if(newColor.equals("orange")){
            colourInt = ContextCompat.getColor(context, R.color.routeOrange);
        }else if(newColor.equals("yellow")){
            colourInt = ContextCompat.getColor(context, R.color.routeYellow);
        }else{
            colourInt = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        }
        return colourInt;

    }



}
