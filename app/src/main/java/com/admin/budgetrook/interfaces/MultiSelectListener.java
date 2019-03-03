package com.admin.budgetrook.interfaces;

import android.content.DialogInterface;

import java.util.List;

public interface MultiSelectListener {
    void onClickOk(List<String> dialog, boolean[] id);
    void onClickCancel();
}
