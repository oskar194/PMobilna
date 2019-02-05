package com.admin.budgetrook.dialogs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.admin.budgetrook.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {
    private static final String DATE_ARG = "dateArg";
    private static final String ID_ARG = "idArg";
    private String date;
    private DatePicker datePicker;
    private String id;
    private OnFragmentInteractionListener mListener;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(String date, String id) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putString(DATE_ARG, date);
        args.putString(ID_ARG, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            date = bundle.getString(DATE_ARG);
            id = bundle.getString(ID_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_picker, container, false);
        datePicker = (DatePicker) view.findViewById(R.id.fragment_datepicker);
        if (date != null) {
            Date currDate = new Date();
            try {
                currDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currDate);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);
                datePicker.updateDate(year, month, day);
            } catch (ParseException e) {
                Log.d("Exception", e.getMessage());
            }
        }
        Button ok = (Button) view.findViewById(R.id.date_fragment_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.set(
                        datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        0, 0, 0
                );
                mListener.onFragmentInteraction(cal.getTime(), id);
            }
        });
        Button cancel = (Button) view.findViewById(R.id.date_fragment_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.finishDialog();
            }
        });
        return view;
    }

    public void onButtonPressed(Date date) {
        if (mListener != null) {
            mListener.onFragmentInteraction(date, id);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            Log.d("BUDGETROOK", "Fragment created callback");
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            Log.d("BUDGETROOK", "Fragment created callback");
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Date date, String id);

        void finishDialog();
    }


}
