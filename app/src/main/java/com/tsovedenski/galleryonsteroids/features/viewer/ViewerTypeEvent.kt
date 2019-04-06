package com.tsovedenski.galleryonsteroids.features.viewer

import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
sealed class ViewerTypeEvent {
    data class OnStart(val media: Media) : ViewerTypeEvent()
    object OnResume : ViewerTypeEvent()
    object OnDestroy : ViewerTypeEvent()

    object SeekStarted : ViewerTypeEvent()
    object SeekEnded : ViewerTypeEvent()
    object TogglePlaying : ViewerTypeEvent()
    data class ProgressChanged(val value: Int, val force: Boolean) : ViewerTypeEvent()
}