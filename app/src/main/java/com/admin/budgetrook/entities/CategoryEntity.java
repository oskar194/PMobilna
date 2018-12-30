package com.admin.budgetrook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity(indices = {@Index(value = {"name", "accountId"}, unique = true)})
public class CategoryEntity {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "name")
    private String name;
    private boolean isSynchronized;
    @ColumnInfo(name = "accountId")
    private int accountId;
    private long externalId;

    public CategoryEntity(String name) {
        this.setName(name);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean aSynchronized) {
        isSynchronized = aSynchronized;
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

    public static CategoryEntity[] prepareData() {
        return new CategoryEntity[]{
                new CategoryEntity("Food"),
                new CategoryEntity("Entertainment"),
                new CategoryEntity("Education")
        };
    }

    @Override
    public String toString() {
        return "CategoryEntity{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", isSynchronized=" + isSynchronized +
                ", accountId=" + accountId +
                ", externalId=" + externalId +
                '}';
    }
}
