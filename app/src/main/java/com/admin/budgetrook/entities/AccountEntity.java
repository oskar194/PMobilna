package com.admin.budgetrook.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = {"login"}, unique = true)})
public class AccountEntity {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "login")
    private String login;
    @ColumnInfo(name = "password")
    private String password;
    private long externalId;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
    }

    public static AccountEntity prepareData() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.login = "Admin";
        accountEntity.password = "adm123";
        return accountEntity;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "uid=" + uid +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", externalId=" + externalId +
                '}';
    }
}
