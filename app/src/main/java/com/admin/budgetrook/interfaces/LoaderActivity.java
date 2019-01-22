package com.admin.budgetrook.interfaces;

import android.content.Context;

public interface LoaderActivity {

    void loaderOn();

    void loaderOff();

    void showMessage(String message);

    Context getContext();
}
