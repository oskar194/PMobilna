package com.admin.budgetrook.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.admin.budgetrook.entities.AccountEntity;

public class PrefsHelper {

    private static PrefsHelper INSTANCE;

    private static final String PREF_NAME = "budgetRookPrefs";
    private static final String LOGIN_KEY = "budgetLogin";

    private PrefsHelper() {
    }

    ;

    public static PrefsHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PrefsHelper();
        }
        return INSTANCE;
    }

    public void loginUser(Context ctx, AccountEntity accountEntity) {
        if (!isUserLogged(ctx)) {
            saveValue(ctx, LOGIN_KEY, accountEntity.getLogin());
        }
    }

    public void logoutUser(Context ctx, AccountEntity accountEntity) {
        if (isUserLogged(ctx)) {
            saveValue(ctx, LOGIN_KEY, null);
        }
    }

    public Boolean isUserLogged(Context ctx) {
        String login = getValue(ctx, LOGIN_KEY);
        return (login != null);
    }

    public String getCurrentUserLogin(Context ctx) {
        return getValue(ctx, LOGIN_KEY);
    }

    private static String getValue(Context ctx, String key) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    private static void saveValue(Context ctx, String key, String value) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(key, value).apply();
    }
}
