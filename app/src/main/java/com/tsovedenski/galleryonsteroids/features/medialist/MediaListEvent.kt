package com.tsovedenski.galleryonsteroids.features.medialist

import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
sealed class MediaListEvent {
    object OnStart : MediaListEvent()
    object OnResume : MediaListEvent()
    object OnOptionsReady : MediaListEvent()
    object OnDestroy : MediaListEvent()

    object CreatePhoto : MediaListEvent()
    object CreateVideo : MediaListEvent()
    object CreateAudio : MediaListEvent()

    data class ChangeViewType(val value: ViewType) : MediaListEvent()
    data class ItemSelected(val value: Media) : MediaListEvent()
}