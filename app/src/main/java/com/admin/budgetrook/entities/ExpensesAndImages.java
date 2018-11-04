package com.admin.budgetrook.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class ExpensesAndImages {
    @Embedded
    ExpenseEntity expense;
    @Relation(parentColumn = "uid", entityColumn = "expenseId")
    private List<ImageEntity> images;

    public ExpenseEntity getExpense() {
        return expense;
    }

    public void setExpense(ExpenseEntity expense) {
        this.expense = expense;
    }

    public List<ImageEntity> getImages() {
        return images;
    }

    public void setImages(List<ImageEntity> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ExpensesAndImages{" +
                "expense=" + expense +
                ", images=" + images +
                '}';
    }
}
