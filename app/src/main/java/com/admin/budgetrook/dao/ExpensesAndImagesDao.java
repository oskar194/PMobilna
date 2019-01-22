package com.admin.budgetrook.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.admin.budgetrook.entities.ExpensesAndImages;

import java.util.List;

@Dao
public interface ExpensesAndImagesDao {
    @Transaction
    @Query("SELECT * FROM ExpenseEntity where accountId = :accountId")
    public List<ExpensesAndImages> getAll(long accountId);

    @Transaction
    @Query("SELECT * FROM ExpenseEntity WHERE uid = :expenseUid and accountId = :accountId")
    public ExpensesAndImages getByExpenseUid(long expenseUid, long accountId);
}
