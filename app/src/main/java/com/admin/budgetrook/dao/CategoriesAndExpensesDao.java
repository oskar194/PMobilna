package com.admin.budgetrook.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.admin.budgetrook.entities.CategoriesAndExpenses;

import java.util.List;

@Dao
public interface CategoriesAndExpensesDao {
    @Transaction
    @Query("SELECT * from CategoryEntity")
    public List<CategoriesAndExpenses> getAll();
}
