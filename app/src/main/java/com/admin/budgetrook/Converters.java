package com.admin.budgetrook;

import android.arch.persistence.room.TypeConverter;

import java.math.BigDecimal;
import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static float toFloat(BigDecimal value) {
        return value == null ? null : value.floatValue();
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(float value) {
        return new BigDecimal(value);
    }
}