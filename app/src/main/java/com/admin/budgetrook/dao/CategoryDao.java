package com.admin.budgetrook.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.admin.budgetrook.entities.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM categoryEntity")
    List<CategoryEntity> getAll();

    @Query("select * from categoryentity where name =:name")
    CategoryEntity getByName(String name);

    @Query("SELECT COUNT(*) FROM CategoryEntity")
    int count();

    @Query("SELECT * from categoryentity where isSynchronized = 0")
    List<CategoryEntity> getNotSynchronized();

    @Query("SELECT * from categoryentity where uid = :uid")
    CategoryEntity getById(int uid);

    @Insert
    void insertAll(CategoryEntity... categories);
}
