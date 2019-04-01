package com.tsovedenski.galleryonsteroids.features.creator

import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
interface CreatorContract {
    interface View {
        fun setObserver(observer: Observer<CreatorEvent>)
        fun setMediaType(value: MediaType)

        // todo: not really part of view
        fun startRecording()
        fun stopRecording()
    }

    interface ViewModel {
        fun getMediaType(): MediaType?
        fun setMediaType(value: MediaType)

        fun isRecording(): Boolean
        fun setRecording(value: Boolean)
    }
}