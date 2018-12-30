package com.admin.budgetrook.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class ExpenseEntity {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    private BigDecimal amount;
    private int categoryId;
    private String name;
    private Date date;
    private boolean isSynchronized;
    private boolean isReviewed;
    private int accountId;
    private long externalId;

    public ExpenseEntity(BigDecimal amount, String name, int categoryId, Date date, Boolean isReviewed, Boolean isSynchronized, int accountId) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.name = name;
        this.date = date;
        this.isReviewed = isReviewed;
        this.isSynchronized = isSynchronized;
        this.accountId = accountId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean aSynchronized) {
        isSynchronized = aSynchronized;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
    }

    public static ExpenseEntity[] prepareData() {
        return new ExpenseEntity[]{
                new ExpenseEntity(new BigDecimal(50.25), "Restaurant", 1,
                        new Date(), true, true, 1),
                new ExpenseEntity(new BigDecimal(100.75), "Book", 2,
                        new Date(), true, true, 1),
                new ExpenseEntity(new BigDecimal(75.55), "School", 3,
                        new Date(), true, true,1)
        };
    }

    @Override
    public String toString() {
        return "ExpenseEntity{" +
                "uid=" + uid +
                ", amount=" + amount +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", isSynchronized=" + isSynchronized +
                ", isReviewed=" + isReviewed +
                ", accountId=" + accountId +
                ", externalId=" + externalId +
                '}';
    }
}
