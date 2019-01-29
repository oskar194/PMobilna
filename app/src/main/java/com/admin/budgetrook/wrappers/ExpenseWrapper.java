package com.admin.budgetrook.wrappers;

import com.admin.budgetrook.entities.ExpenseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseWrapper {

    public Double amount;
    public String categoryName;
    public String name;
    public Date dateAdded;
    public String userEmail;
    public List<ImageWrapper> images;
    public Long externalId;

    public Long categoryUid;
    public Long accountUid;
    public Long uid;

    public ExpenseWrapper(ExpenseEntity expenseEntity, String username, String categoryName) {
        amount = expenseEntity.getAmount().doubleValue();
        name = expenseEntity.getName();
        dateAdded = expenseEntity.getDate();
        userEmail = username;
        images = new ArrayList<ImageWrapper>();
        this.categoryName = categoryName;
        accountUid = expenseEntity.getAccountId();
        categoryUid = expenseEntity.getCategoryId();
        uid = expenseEntity.getUid();
    }
}
