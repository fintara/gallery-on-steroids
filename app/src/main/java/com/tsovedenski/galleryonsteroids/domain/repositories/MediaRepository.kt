package com.tsovedenski.galleryonsteroids.domain.repositories

import androidx.room.*
import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
@Dao
interface MediaRepository {
    @Query("SELECT * FROM media ORDER BY created_at")
    suspend fun getMedia(): List<Media>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(media: Media)

    @Delete
    suspend fun delete(media: Media)
}