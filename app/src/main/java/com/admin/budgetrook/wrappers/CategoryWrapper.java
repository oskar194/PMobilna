package com.admin.budgetrook.wrappers;

import com.admin.budgetrook.entities.CategoryEntity;

import java.util.List;

public class CategoryWrapper {

    public String name;
    public String userEmail;
    public Long externalId;
    public Long accountId;
    public List<ExpenseWrapper> expenses;
    public Long uid;

    public CategoryWrapper(CategoryEntity entity, String userEmail){
        name = entity.getName();
        externalId = entity.getUid();
        this.userEmail = userEmail;
        accountId = entity.getAccountId();
        uid = entity.getUid();
    }
}
