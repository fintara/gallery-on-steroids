package com.tsovedenski.galleryonsteroids.domain

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.repositories.MediaRepository
import com.tsovedenski.galleryonsteroids.domain.typeconverters.InstantTypeConverter
import com.tsovedenski.galleryonsteroids.domain.typeconverters.MediaTypeTypeConverter

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
interface AppDatabase {
    fun getMediaRepository(): MediaRepository
}

@Database(
    entities = [Media::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(InstantTypeConverter::class, MediaTypeTypeConverter::class)
abstract class AppDatabaseImpl : RoomDatabase(), AppDatabase