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

        fun play()
        fun pause()
        fun seek(msec: Int, force: Boolean)
    }

    interface ViewModel {
        fun getMedia(): Media?
        fun setMedia(value: Media?)

        fun isPlaying(): Boolean
        fun setPlaying(value: Boolean)

        fun getProgress(): Int
        fun setProgress(value: Int)
    }
}