package com.admin.budgetrook.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.admin.budgetrook.entities.ExpenseEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface ExpenseDao {
    @Query("Select * from expenseEntity")
    List<ExpenseEntity> getAll();

    @Insert
    void insertAll(ExpenseEntity... expenses);

    @Insert
    long insert(ExpenseEntity expense);

    @Query("Select * from expenseEntity where date between :dateFrom and :dateTo")
    List<ExpenseEntity> getBetween(Date dateFrom, Date dateTo);

    @Query("select * from expenseentity where uid = :uid")
    ExpenseEntity getById(int uid);

    @Query("select * from expenseentity where categoryId= :categoryId")
    List<ExpenseEntity> getByCategoryId(int categoryId);

    @Query("select sum(amount) from expenseentity")
    Long expensesSum();

    @Query("select * from expenseentity where isReviewed = 0")
    List<ExpenseEntity> getNotReviewed();

    @Query("select * from expenseentity where isSynchronized = 0")
    List<ExpenseEntity> getNotSynchronized();

    @Query("select count(uid) from expenseentity where isSynchronized = 1 and isReviewed = 0")
    int getNumberOfExpensesToReview();

    @Update
    void update(ExpenseEntity expense);

    @Delete
    void delete(ExpenseEntity expenseEntity);
}
