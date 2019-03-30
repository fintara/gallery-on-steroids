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
    }

    interface ViewModel {
        fun getMediaType(): MediaType?
        fun setMediaType(value: MediaType)
    }
}