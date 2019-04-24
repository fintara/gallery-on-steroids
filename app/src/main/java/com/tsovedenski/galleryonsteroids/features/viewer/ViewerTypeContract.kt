package com.tsovedenski.galleryonsteroids.features.viewer

import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
interface ViewerTypeContract {
    interface View {
        fun setObserver(observer: Observer<ViewerTypeEvent>)

        fun prepare(media: Media)

        fun play() = Unit
        fun pause() = Unit
        fun seek(msec: Int, force: Boolean) = Unit

        fun showControls()
        fun hideControls()

        fun showReplayButton() = Unit
        fun hideReplayButton() = Unit
    }

    interface ViewModel {
        var media: Media?
        var playingState: PlayingState
        var progress: Int
        var isControlShown: Boolean
    }
}

sealed class PlayingState {
    object Paused : PlayingState()
    object Playing : PlayingState()
    object Completed : PlayingState()
}