package com.tsovedenski.galleryonsteroids.features.photoeditor

import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import java.io.File

/**
 * Created by Tsvetan Ovedenski on 20/04/2019.
 */
interface PhotoEditorContract {
    interface View {
        fun setObserver(observer: Observer<PhotoEditorEvent>)

        fun setMedia(media: Media)

        fun openCrop(mediaUri: String)
        fun openTune(mediaUri: String)
        fun openStyle(mediaUri: String)

        fun showLoader()
        fun hideLoader()
    }

    interface ViewModel {
        var media: Media?
        var tempFile: File?
    }
}