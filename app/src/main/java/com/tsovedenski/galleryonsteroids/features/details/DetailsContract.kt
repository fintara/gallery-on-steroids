package com.tsovedenski.galleryonsteroids.features.details

import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media

interface DetailsContract {
    interface View {
        fun setObserver(observer: Observer<DetailsEvent>)
    }

    interface ViewModel {
        fun getMedia(): Media?
        fun setMedia(value: Media)
    }
}