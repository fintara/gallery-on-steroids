package com.tsovedenski.galleryonsteroids.services

import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.domain.repositories.MediaRepository
import java.io.File

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class MediaService (
    private val mediaRepository: MediaRepository
) {

    suspend fun getMedia(): List<Media> = mediaRepository.getMedia()

    suspend fun save(media: Media): Media {
        mediaRepository.save(media)
        return media
    }

    suspend fun delete(media: Media) {
        File(media.path).delete()
        mediaRepository.delete(media)
    }
}