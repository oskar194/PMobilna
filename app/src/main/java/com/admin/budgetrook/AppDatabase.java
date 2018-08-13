package com.admin.budgetrook;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.admin.budgetrook.dao.AccountDao;
import com.admin.budgetrook.dao.CategoriesAndExpensesDao;
import com.admin.budgetrook.dao.CategoryDao;
import com.admin.budgetrook.dao.ExpenseDao;
import com.admin.budgetrook.entities.AccountEntity;
import com.admin.budgetrook.entities.CategoriesAndExpenses;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.entities.ExpenseEntity;

import java.util.concurrent.Executors;

@Database(entities = {AccountEntity.class, CategoryEntity.class, ExpenseEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDao accountDao();

    public abstract ExpenseDao expenseDao();

    public abstract CategoryDao categoryDao();

    public abstract CategoriesAndExpensesDao categoriesAndExpensesDao();

    private static AppDatabase INSTANCE;

    public synchronized static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                    AppDatabase.class,
                    "my-database")
                    .fallbackToDestructiveMigration()
                    .build();
            INSTANCE.populateData();
        }
        return INSTANCE;
    }

    private void populateData() {
        if (categoryDao().count() < 1) {
            CategoryEntity[] categories = CategoryEntity.prepareData();
            ExpenseEntity[] expenses = ExpenseEntity.prepareData();
            beginTransaction();
            try {
                categoryDao().insertAll(categories);
                expenseDao().insertAll(expenses);
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }
}
