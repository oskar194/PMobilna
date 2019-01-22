package com.admin.budgetrook.apis;

import com.admin.budgetrook.entities.ExpenseEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface ExpenseApi {
    @POST("/expense")
    Call<ExpenseEntity> postExpense(@Body ExpenseEntity expenseEntity);

    @PATCH("/expense")
    Call<ExpenseEntity> updateExpense(@Body ExpenseEntity expenseEntity);

    @DELETE("/expense")
    Call<ExpenseEntity> deleteExpense(@Body ExpenseEntity expenseEntity);
}
