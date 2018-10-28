package com.admin.budgetrook.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.admin.budgetrook.R;

public class ScreenSlidePageFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        Bundle args = getArguments();
        Log.d("BUDGETROOK", "args" + args);
        Integer number = args.getInt("text");
        TextView tv = (TextView) v.findViewById(R.id.frg_tv);
        tv.setText("Object: " + number);
        return v;
    }
}
