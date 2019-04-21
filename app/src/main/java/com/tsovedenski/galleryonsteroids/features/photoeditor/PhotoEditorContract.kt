package com.tsovedenski.galleryonsteroids.features.photoeditor

import android.graphics.Bitmap
import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import java.io.File

/**
 * Created by Tsvetan Ovedenski on 20/04/2019.
 */
interface PhotoEditorContract {
    interface View {
        fun setObserver(observer: Observer<PhotoEditorEvent>)

        fun setImage(media: Media)
        fun setImage(bitmap: Bitmap)

        fun openCrop(mediaUri: String)
        fun openTune(mediaUri: String)
        fun openStyle(mediaUri: String)

        fun showLoader()
        fun hideLoader()
    }

    interface ViewModel {
        var loaded: Boolean
        var media: Media?
        var tempFile: File?
    }
}