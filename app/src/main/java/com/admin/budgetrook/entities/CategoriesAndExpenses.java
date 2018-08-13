package com.admin.budgetrook.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CategoriesAndExpenses {
    @Embedded
    CategoryEntity category;
    @Relation(parentColumn = "uid", entityColumn = "categoryId")
    private List<ExpenseEntity> expenses;

    public CategoriesAndExpenses(CategoryEntity category) {
        this.category = category;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public List<ExpenseEntity> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseEntity> expenses) {
        this.expenses = expenses;
    }

    @Override
    public String toString() {
        return "CategoriesAndExpenses{" +
                "category=" + category +
                ", expenses=" + expenses +
                '}';
    }
}
