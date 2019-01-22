package com.admin.budgetrook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Arrays;

@Entity
public class ImageEntity {
    @PrimaryKey(autoGenerate = true)
    private long uid;
    private long expenseId;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;
    private long accountId;
    private long externalId;
    private String path;

    public ImageEntity(long expenseId, byte[] image, String path, long accountId) {
        this.expenseId = expenseId;
        this.image = image;
        this.path = path;
        this.accountId = accountId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "uid=" + uid +
                ", expenseId=" + expenseId +
                ", image=" + image.length +
                ", accountId=" + accountId +
                ", externalId=" + externalId +
                ", path='" + path + '\'' +
                '}';
    }
}
