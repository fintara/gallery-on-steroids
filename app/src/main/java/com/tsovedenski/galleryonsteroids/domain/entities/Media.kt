package com.tsovedenski.galleryonsteroids.domain.entities

import android.os.Environment
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.*

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
@Entity(tableName = "media")
data class Media (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "media_type")
    val type: MediaType,

    @ColumnInfo(name = "created_at")
    val createdAt: Instant = Instant.now()
) {
//    val path: String get() = Environment.getDataDirectory().absolutePath + when (type) {
    val path: String get() = Environment.getExternalStorageDirectory().absolutePath + when (type) {
        MediaType.Photo -> "/$id.jpg"
        MediaType.Video -> "/$id.mp4"
        MediaType.Audio -> "/$id.m4a"
    }
}

sealed class MediaType (val asString: String) {
    object Audio : MediaType("audio")
    object Video : MediaType("video")
    object Photo : MediaType("photo")

    override fun toString() = asString

    companion object {
        fun fromString(value: String): MediaType? = when (value.toLowerCase()) {
            Audio.asString -> Audio
            Video.asString -> Video
            Photo.asString -> Photo
            else -> null
        }
    }
}