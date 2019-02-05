package com.admin.budgetrook.interfaces;

import android.content.Context;

public interface DataReceiver {
    Context getContext();
    void receiveData(Object data, String id);
}
