package com.admin.budgetrook.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.admin.budgetrook.entities.AccountEntity;

import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM accountEntity")
    List<AccountEntity> getAll();

    @Insert
    void insertAll(AccountEntity... accounts);
}
