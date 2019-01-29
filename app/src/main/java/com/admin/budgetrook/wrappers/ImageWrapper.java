package com.admin.budgetrook.wrappers;

import android.media.Image;

import com.admin.budgetrook.entities.ImageEntity;

public class ImageWrapper {
    public String name;
    public String bytesBase64;
    public String username;
    public Long externalId;

    public Long uid;
    public Long accountUid;
    public Long expenseId;
    public String path;

    public ImageWrapper(ImageEntity imageEntity, String username) {
        name = "";
        this.username = username;
        externalId = imageEntity.getUid();
        uid = imageEntity.getUid();
        accountUid = imageEntity.getAccountId();
        expenseId = imageEntity.getExpenseId();
        path = imageEntity.getPath();
    }
}
