package com.tsovedenski.galleryonsteroids.features.form

import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media

interface FormContract {
    interface View {
        fun setObserver(observer: Observer<FormEvent>)
        fun setThumbnail(media: Media)
        fun showMessage(@StringRes resId: Int)
        fun close()
    }

    interface ViewModel {
        fun getMedia(): Media?
        fun setMedia(value: Media?)
    }
}