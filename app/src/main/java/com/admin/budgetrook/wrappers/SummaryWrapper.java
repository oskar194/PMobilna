package com.admin.budgetrook.wrappers;

import com.admin.budgetrook.entities.CategoryEntity;
import com.admin.budgetrook.entities.ExpenseEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SummaryWrapper {
    public List<Date> dates;
    public List<ExpenseEntity> expenses;
    public List<CategoryEntity> categories;
    public float sum;
    public Map<String, String> categoriesMap;
}
