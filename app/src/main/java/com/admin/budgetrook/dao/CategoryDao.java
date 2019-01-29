package com.admin.budgetrook.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.admin.budgetrook.entities.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categoryEntity where accountId = :accountId")
    List<CategoryEntity> getAll(long accountId);

    @Query("select * from categoryentity where name =:name and accountId =:accountId LIMIT 1")
    CategoryEntity getByName(String name, long accountId);

    @Query("SELECT COUNT(*) FROM CategoryEntity")
    long count();

    @Query("SELECT * from categoryentity where isSynchronized = 0 and accountId =:accountId")
    List<CategoryEntity> getNotSynchronized(long accountId);

    @Query("SELECT * from categoryentity where uid = :uid and accountId = :accountId LIMIT 1")
    CategoryEntity getById(long uid, long accountId);

    @Update
    void updateAll(CategoryEntity... categoryEntities);

    @Insert
    void insertAll(CategoryEntity... categories);
}
