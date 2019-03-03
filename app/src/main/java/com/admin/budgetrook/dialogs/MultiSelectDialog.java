package com.admin.budgetrook.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.admin.budgetrook.interfaces.MultiSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectDialog extends DialogFragment {
    private List<String> values;
    private MultiSelectListener listener;
    private boolean[] selected;

    private static final String VALUES_PARAM = "values";
    private static final String SELECTED_PARAM = "selected";

    public MultiSelectDialog() {
        // Required empty public constructor
    }

    public static MultiSelectDialog newInstance(ArrayList<String> inputs, boolean[] selectedInputs) {
        MultiSelectDialog fragment = new MultiSelectDialog();
        Bundle args = new Bundle();
        args.putStringArrayList(VALUES_PARAM, inputs);
        args.putBooleanArray(SELECTED_PARAM, selectedInputs);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.getStringArrayList(VALUES_PARAM) != null) {
            values = arguments.getStringArrayList(VALUES_PARAM);
            if (arguments.getBooleanArray(SELECTED_PARAM) != null) {
                selected = arguments.getBooleanArray(SELECTED_PARAM);
            } else {
                selected = new boolean[values.size()];
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select")
                .setMultiChoiceItems(values.toArray(new CharSequence[values.size()]), selected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id, boolean isChecked) {
                        selected[id] = isChecked;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onClickOk(values, selected);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onClickCancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof MultiSelectListener) {
            listener = (MultiSelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MultiSelectListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
