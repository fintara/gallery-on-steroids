package com.tsovedenski.galleryonsteroids.domain.entities

import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
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

    @ColumnInfo(name = "duration")
    val duration: Long? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Instant = Instant.now()
) : Parcelable {
//    val path: String get() = Environment.getDataDirectory().absolutePath + when (type) {
    val path: String get() = Environment.getExternalStorageDirectory().absolutePath + when (type) {
        MediaType.Photo -> "/$id.jpg"
        MediaType.Video -> "/$id.mp4"
        MediaType.Audio -> "/$id.m4a"
    }

    val thumbnailPath: String get() = when (type) {
        MediaType.Audio -> "file:///android_asset/voice_thumbnail.jpg"
        else -> path
    }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        MediaType.fromString(parcel.readString() ?: "") ?: throw RuntimeException("Bad media type"),
        with(parcel.readLong()) { if (this < 1) null else this },
        Instant.ofEpochSecond(parcel.readLong())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(type.asString)
        parcel.writeLong(duration ?: -1)
        parcel.writeLong(createdAt.epochSecond)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Media> {
        override fun createFromParcel(parcel: Parcel): Media {
            return Media(parcel)
        }

        override fun newArray(size: Int): Array<Media?> {
            return arrayOfNulls(size)
        }
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