package com.admin.budgetrook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Arrays;

@Entity
public class ImageEntity {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    private int expenseId;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public ImageEntity(int expenseId, byte[] image, String path) {
        this.expenseId = expenseId;
        this.image = image;
        this.path = path;
    }

    private String path;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "uid=" + uid +
                ", expenseId=" + expenseId +
                ", image=" + image.length +
                ", path='" + path + '\'' +
                '}';
    }
}
