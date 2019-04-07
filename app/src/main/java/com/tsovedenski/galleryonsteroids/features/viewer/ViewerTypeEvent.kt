package com.tsovedenski.galleryonsteroids.features.viewer

import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
sealed class ViewerTypeEvent {
    data class OnStart(val media: Media) : ViewerTypeEvent()
    object OnResume : ViewerTypeEvent()
    object OnDestroy : ViewerTypeEvent()

    object MediaClicked : ViewerTypeEvent()

    object SeekStarted : ViewerTypeEvent()
    object SeekEnded : ViewerTypeEvent()
    object TogglePlaying : ViewerTypeEvent()
    object Replay : ViewerTypeEvent()
    object PlayingCompleted : ViewerTypeEvent()
    data class ProgressChanged(val value: Int, val force: Boolean) : ViewerTypeEvent()
}