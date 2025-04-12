package com.interview.ideamanager.data

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromLongToDate(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }

    @TypeConverter
    fun fromDateToLong(date: java.util.Date?): Long? {
        return date?.time
    }
}