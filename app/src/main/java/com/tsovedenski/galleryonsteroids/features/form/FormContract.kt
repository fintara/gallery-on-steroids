package com.tsovedenski.galleryonsteroids.features.form

import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media

interface FormContract {
    interface View {
        fun setObserver(observer: Observer<FormEvent>)
        fun setThumbnail(media: Media)
        fun close()
    }

    interface ViewModel {
        fun getMedia(): Media?
        fun setMedia(value: Media?)
    }
}