package com.tsovedenski.galleryonsteroids.features.details

import com.tsovedenski.galleryonsteroids.domain.entities.Media

sealed class DetailsEvent {
    data class OnStart (val media: Media) : DetailsEvent()
    object OnResume : DetailsEvent()
    object OnDestroy : DetailsEvent()

    data class Save(val title: String) : DetailsEvent()
}