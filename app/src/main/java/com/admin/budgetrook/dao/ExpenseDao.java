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
    @Query("Select * from expenseEntity where accountId = :accountId")
    List<ExpenseEntity> getAll(long accountId);

    @Insert
    void insertAll(ExpenseEntity... expenses);

    @Insert
    long insert(ExpenseEntity expense);

    @Query("Select * from expenseEntity where (date between :dateFrom and :dateTo) and accountId =:accountId and isReviewed = 1 order by date asc ")
    List<ExpenseEntity> getBetween(Date dateFrom, Date dateTo, long accountId);

    @Query("select * from expenseentity where uid = :uid and accountId =:accountId")
    ExpenseEntity getById(long uid, long accountId);

    @Query("select * from expenseentity where categoryId= :categoryId and accountId =:accountId")
    List<ExpenseEntity> getByCategoryId(long categoryId, long accountId);

    @Query("select sum(amount) from expenseentity where accountId = :accountId")
    Long expensesSum(long accountId);

    @Query("select * from expenseentity where isReviewed = 0 and accountId = :accountId")
    List<ExpenseEntity> getNotReviewed(long accountId);

    @Query("select * from expenseentity where isSynchronized = 0 and accountId = :accountId")
    List<ExpenseEntity> getNotSynchronized(long accountId);

    @Query("select count(uid) from expenseentity where isSynchronized = 1 and isReviewed = 0 and accountId = :accountId")
    int getNumberOfExpensesToReview(long accountId);

    @Update
    void update(ExpenseEntity expense);

    @Delete
    void delete(ExpenseEntity expenseEntity);
}
