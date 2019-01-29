package com.admin.budgetrook.wrappers;

import com.admin.budgetrook.entities.ExpenseEntity;
import com.admin.budgetrook.entities.ImageEntity;

public class AggregatedExpense {
    private ExpenseEntity expenseEntity;
    private ImageEntity imageEntity;
    private String categoryName;

    public AggregatedExpense(ExpenseEntity expenseEntity, ImageEntity imageEntity, String categoryName) {

        this.expenseEntity = expenseEntity;
        this.imageEntity = imageEntity;
        this.categoryName = categoryName;
    }

    public ExpenseEntity getExpenseEntity() {
        return expenseEntity;
    }

    public void setExpenseEntity(ExpenseEntity expenseEntity) {
        this.expenseEntity = expenseEntity;
    }

    public ImageEntity getImageEntity() {
        return imageEntity;
    }

    public void setImageEntity(ImageEntity imageEntity) {
        this.imageEntity = imageEntity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
