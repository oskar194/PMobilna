package com.admin.budgetrook.dialogs;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.admin.budgetrook.R;

public class AmountPickerDialog extends DialogFragment {
    private static final String AMOUNT_PARAM= "amount";

    private String decAmount;
    private String fracAmount;
    private NumberPicker decPicker;
    private NumberPicker fracPicker;
    private AmountPickerInterface mListener;

    public AmountPickerDialog() {
        // Required empty public constructor
    }

    public static AmountPickerDialog newInstance(String amount) {
        AmountPickerDialog fragment = new AmountPickerDialog();
        Bundle args = new Bundle();
        args.putString(AMOUNT_PARAM, amount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String raw = getArguments().getString(AMOUNT_PARAM);
            Log.d("BUDGETROOK", "raw: " +raw);
            String[] parts = raw.split("\\.");
            Log.d("BUDGETROOK", "splited: " + parts);
            decAmount = parts[0];
            Log.d("BUDGETROOK", "splited: " + decAmount);
            fracAmount = parts[1];
            Log.d("BUDGETROOK", "splited: " + fracAmount);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_amount_picker_dialog, container, false);
        decPicker = (NumberPicker) v.findViewById(R.id.amount_dialog_dec_picker);
        decPicker.setMinValue(0);
        decPicker.setMaxValue(Integer.MAX_VALUE);
        decPicker.setWrapSelectorWheel(false);
        decPicker.setValue(Integer.parseInt(decAmount));
        fracPicker = (NumberPicker) v.findViewById(R.id.amount_dialog_frac_picker);
        fracPicker.setMinValue(0);
        fracPicker.setMaxValue(99);
        fracPicker.setWrapSelectorWheel(false);
        fracPicker.setValue(Integer.parseInt(fracAmount));

        Button ok = (Button)v.findViewById(R.id.amount_dialog_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewAmount();
            }
        });
        Button cancel = (Button)v.findViewById(R.id.amount_dialog_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewAmount();
            }
        });
        return v;
    }

    public void sendNewAmount() {
        int decPart = decPicker.getValue();
        int fracPart = fracPicker.getValue();
        String newAmount = "";
        if(fracPart < 10){
        newAmount = decPart + ".0" + fracPart;
        } else {
            newAmount = decPart + "." + fracPart;
        }
        if (mListener != null) {
            mListener.amountSelected(newAmount);
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof AmountPickerInterface) {
            mListener = (AmountPickerInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AmountPickerInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface AmountPickerInterface {
        void amountSelected(String newAmount);
    }
}
