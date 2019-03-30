package com.tsovedenski.galleryonsteroids.features.creator

import com.tsovedenski.galleryonsteroids.domain.entities.MediaType

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
sealed class CreatorEvent {
    data class OnStart(val initialMediaType: MediaType) : CreatorEvent()
    object OnResume : CreatorEvent()
    object OnDestroy : CreatorEvent()

    data class ChangeType(val value: MediaType) : CreatorEvent()
}