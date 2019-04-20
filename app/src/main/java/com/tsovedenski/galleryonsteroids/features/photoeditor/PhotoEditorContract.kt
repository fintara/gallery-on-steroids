package com.tsovedenski.galleryonsteroids.features.photoeditor

import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 20/04/2019.
 */
interface PhotoEditorContract {
    interface View {
        fun setObserver(observer: Observer<PhotoEditorEvent>)
    }

    interface ViewModel {
        var media: Media?
    }
}