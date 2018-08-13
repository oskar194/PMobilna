package com.admin.budgetrook;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DbHelper {
    private static AppDatabase db;

    public static AppDatabase getDb(Context context){
        if(db == null){
            db = Room.databaseBuilder(context,
                    AppDatabase.class, "database-name").build();
        }
        return db;
    }
}
