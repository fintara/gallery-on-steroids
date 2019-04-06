package com.tsovedenski.galleryonsteroids.features.form

import com.tsovedenski.galleryonsteroids.domain.entities.Media

sealed class FormEvent {
    data class OnStart (val media: Media) : FormEvent()
    object OnResume : FormEvent()
    object OnDestroy : FormEvent()

    data class Save(val title: String) : FormEvent()
}