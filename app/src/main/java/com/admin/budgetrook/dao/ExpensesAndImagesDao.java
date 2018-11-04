package com.admin.budgetrook.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.admin.budgetrook.entities.ExpensesAndImages;

import java.util.List;

@Dao
public interface ExpensesAndImagesDao {
    @Transaction
    @Query("SELECT * FROM ExpenseEntity")
    public List<ExpensesAndImages> getAll();

    @Transaction
    @Query("SELECT * FROM ExpenseEntity WHERE uid = :expenseUid")
    public ExpensesAndImages getByExpenseUid(int expenseUid);
}
