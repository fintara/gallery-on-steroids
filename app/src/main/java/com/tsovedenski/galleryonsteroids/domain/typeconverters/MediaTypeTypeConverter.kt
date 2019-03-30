package com.tsovedenski.galleryonsteroids.domain.typeconverters

import androidx.room.TypeConverter
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class MediaTypeTypeConverter {
    @TypeConverter
    fun mediaTypeToString(value: MediaType): String = value.asString

    @TypeConverter
    fun stringToMediaType(value: String): MediaType = MediaType.fromString(value)
        ?: throw RuntimeException("Unknown media type: $value")
}