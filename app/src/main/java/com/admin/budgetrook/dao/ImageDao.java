package com.admin.budgetrook.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.admin.budgetrook.entities.ImageEntity;

import java.util.List;

@Dao
public interface ImageDao {

    @Query("SELECT * FROM imageEntity")
    List<ImageEntity> getAll();

    @Query("SELECT * FROM imageentity WHERE expenseId = :expenseUid")
    ImageEntity getByExpenseId(int expenseUid);

    @Insert
    void insert(ImageEntity imageEntity);

    @Insert
    void insertAll(ImageEntity... imageEntities);

    @Delete
    void delete(ImageEntity entity);

    @Update
    void update(ImageEntity imageEntity);
}
