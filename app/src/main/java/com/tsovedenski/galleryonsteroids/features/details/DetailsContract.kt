package com.tsovedenski.galleryonsteroids.features.details

import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media

interface DetailsContract {
    interface View {
        fun setObserver(observer: Observer<DetailsEvent>)
        fun setThumbnail(media: Media)
        fun close()
    }

    interface ViewModel {
        fun getMedia(): Media?
        fun setMedia(value: Media?)
    }
}