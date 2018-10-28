package com.admin.budgetrook.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.admin.budgetrook.R;
import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.interfaces.ChartFragmentInterface;

import java.util.List;

public class RadarChartFragment extends Fragment{

    private List<CategoriesAndExpenses> chartData;
    private ChartFragmentInterface mListener;

    public RadarChartFragment() {
        // Required empty public constructor
    }

    public static RadarChartFragment newInstance() {
        RadarChartFragment fragment = new RadarChartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_radar_chart, container, false);
        setup();
        return v;
    }

    public void setup() {
        if (mListener != null) {
            chartData = mListener.getData();
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof ChartFragmentInterface) {
            mListener = (ChartFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ChartFragmentInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
