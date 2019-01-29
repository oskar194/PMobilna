package com.admin.budgetrook.apis;

import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.wrappers.ExpenseWrapper;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface ExpenseApi {
    @POST("/expense")
    Call<ExpenseWrapper> postExpense(@Body ExpenseWrapper expenseWrapper);

    @PATCH("/expense")
    Call<ExpenseWrapper> updateExpense(@Body ExpenseWrapper expenseWrapper);

    @DELETE("/expense")
    Call<ExpenseWrapper> deleteExpense(@Body ExpenseWrapper expenseWrapper);
}
