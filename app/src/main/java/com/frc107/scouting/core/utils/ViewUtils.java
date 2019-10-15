package com.frc107.scouting.core.utils;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.frc107.scouting.core.ScoutingStrings;

public class ViewUtils {
    private ViewUtils() {}

    public static boolean requestFocus(View v, Activity activity){
        if(v.requestFocus()){
            activity.getWindow().setSoftInputMode((WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE));
            return true;
        }
        return false;
    }

    public static void requestFocusToUnfinishedQuestion(View view, Activity activity) {
        requestFocus(view, activity);
        Toast.makeText(activity.getApplicationContext(), ScoutingStrings.UNFINISHED_QUESTION_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    public static void setRadioGroupEnabled(RadioGroup radioGroup, boolean enabled) {
        radioGroup.setEnabled(enabled);
        int childCount = radioGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RadioButton button = (RadioButton) radioGroup.getChildAt(i);
            button.setEnabled(enabled);
        }
    }
}