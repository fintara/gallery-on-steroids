package com.tsovedenski.galleryonsteroids.domain.typeconverters

import androidx.room.TypeConverter
import java.time.Instant

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class InstantTypeConverter {
    @TypeConverter
    fun instantToLong(value: Instant): Long = value.toEpochMilli()

    @TypeConverter
    fun longToInstant(value: Long): Instant = Instant.ofEpochMilli(value)
}