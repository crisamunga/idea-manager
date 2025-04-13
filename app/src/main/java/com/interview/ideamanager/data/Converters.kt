package com.interview.ideamanager.data

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromLongToDate(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun fromDateToLong(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}