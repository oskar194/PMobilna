package com.admin.budgetrook;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.admin.budgetrook.dao.AccountDao;
import com.admin.budgetrook.dao.CategoriesAndExpensesDao;
import com.admin.budgetrook.dao.CategoryDao;
import com.admin.budgetrook.dao.ExpenseDao;
import com.admin.budgetrook.dao.ExpensesAndImagesDao;
import com.admin.budgetrook.dao.ImageDao;
import com.admin.budgetrook.entities.AccountEntity;
import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.entities.ImageEntity;

@Database(entities = {AccountEntity.class, CategoryEntity.class, ExpenseEntity.class, ImageEntity.class}, version = 7, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDao accountDao();

    public abstract ExpenseDao expenseDao();

    public abstract CategoryDao categoryDao();

    public abstract ImageDao imageDao();

    public abstract CategoriesAndExpensesDao categoriesAndExpensesDao();

    public abstract ExpensesAndImagesDao expensesAndImagesDao();

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
            AccountEntity accountEntity = AccountEntity.prepareData();
            CategoryEntity[] categories = CategoryEntity.prepareData();
            ExpenseEntity[] expenses = ExpenseEntity.prepareData();
            beginTransaction();
            try {
                accountDao().insertAll(accountEntity);
                categoryDao().insertAll(categories);
                expenseDao().insertAll(expenses);
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }
}
