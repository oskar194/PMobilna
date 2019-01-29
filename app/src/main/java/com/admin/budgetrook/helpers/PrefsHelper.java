package com.admin.budgetrook.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.admin.budgetrook.entities.AccountEntity;

public class PrefsHelper {

    private static PrefsHelper INSTANCE;
    private static SharedPreferences PREFS;
    private static Boolean isUserLogged = false;

    private static final String PREF_NAME = "budgetRookPrefs";
    private static final String LOGIN_KEY = "budgetLogin";
    private static final String ID_KEY = "budgetId";
    private static final String COOKIE_KEY = "cookie";

    private PrefsHelper() {
    }

    public static PrefsHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PrefsHelper();
        }
        return INSTANCE;
    }

    private SharedPreferences getPrefs(Context ctx) {
        if (PREFS == null) {
            PREFS = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        return PREFS;
    }

    public Boolean isUserLogged(Context ctx) {
        if (!isUserLogged) {
            SharedPreferences prefs = getPrefs(ctx);
            isUserLogged = (prefs.getString(LOGIN_KEY, null) != null);
            isUserLogged &= (prefs.getInt(ID_KEY, 0) != 0);
        }
        return isUserLogged;
    }

    public void loginUser(Context ctx, AccountEntity accountEntity) {
        getPrefs(ctx).edit()
                .putString(LOGIN_KEY, accountEntity.getLogin())
                .putLong(ID_KEY, accountEntity.getUid())
                .apply();
        isUserLogged = true;
    }

    public void logoutUser(Context ctx, AccountEntity accountEntity) {
        if (isUserLogged(ctx)) {
            getPrefs(ctx).edit()
                    .putString(LOGIN_KEY, null)
                    .putLong(ID_KEY, 0)
                    .putString(COOKIE_KEY, null)
                    .apply();
            isUserLogged = false;
        }
    }

    public String getCurrentUserLogin(Context ctx) {
        return getPrefs(ctx).getString(LOGIN_KEY, null);
    }

    public String getCurrentCookie(Context ctx) {
        return getPrefs(ctx).getString(COOKIE_KEY, null);
    }

    public void setCookie(Context ctx, String cookie) {
        getPrefs(ctx).edit().putString(COOKIE_KEY, cookie).apply();
    }

    public Long getCurrentUserId(Context ctx) {
        return getPrefs(ctx).getLong(ID_KEY, 0);
    }
}
