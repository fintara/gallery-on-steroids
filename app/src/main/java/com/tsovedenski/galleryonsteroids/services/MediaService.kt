package com.tsovedenski.galleryonsteroids.services

import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.domain.repositories.MediaRepository

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class MediaService (
    private val mediaRepository: MediaRepository
) {

    suspend fun getMedia(): List<Media> = mediaRepository.getMedia()

    suspend fun savePicture(): Media {
        val media = Media(
            title = "Test manual",
            type = MediaType.Picture
        )

        mediaRepository.save(media)

        return media
    }

}