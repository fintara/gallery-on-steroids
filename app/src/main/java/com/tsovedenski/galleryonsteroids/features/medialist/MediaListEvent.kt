package com.tsovedenski.galleryonsteroids.features.medialist

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
sealed class MediaListEvent {
    object OnStart : MediaListEvent()
    object OnResume : MediaListEvent()
    object OnDestroy : MediaListEvent()

    object CreatePhoto : MediaListEvent()
    object CreateVideo : MediaListEvent()
    object CreateAudio : MediaListEvent()

    data class ChangeViewType(val value: ViewType) : MediaListEvent()
}