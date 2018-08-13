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

@Entity(indices = {@Index(value = {"name"}, unique = true)})
public class CategoryEntity {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "name")
    private String name;
    private boolean isSynchronized;

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
                '}';
    }
}
