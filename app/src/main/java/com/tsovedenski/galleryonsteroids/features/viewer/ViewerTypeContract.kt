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
        fun getMedia(): Media?
        fun setMedia(value: Media?)

        fun getPlayingState(): PlayingState
        fun setPlayingState(value: PlayingState)

        fun getProgress(): Int
        fun setProgress(value: Int)

        fun isControlShown(): Boolean
        fun setControlShown(value: Boolean)
    }
}

sealed class PlayingState {
    object Paused : PlayingState()
    object Playing : PlayingState()
    object Completed : PlayingState()
}