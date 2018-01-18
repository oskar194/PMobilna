package com.admin.budgetrook;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Admin on 11.01.2018.
 */
@SuppressWarnings("unchecked")
public class LayoutHelper {
    public static <T extends View> T getById(Activity activity, int id){
        return ((T)activity.findViewById(id));
    }
}
