package com.tsovedenski.galleryonsteroids.features.photoslist

import androidx.lifecycle.Observer

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
interface PhotosListContract {
    interface View {
        fun setObserver(observer: Observer<PhotosListEvent>)
    }

    interface ViewModel {
        fun isLoaded(): Boolean
        fun setLoaded(value: Boolean)
    }
}
