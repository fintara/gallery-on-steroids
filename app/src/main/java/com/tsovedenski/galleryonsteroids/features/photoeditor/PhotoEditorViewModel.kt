package com.tsovedenski.galleryonsteroids.features.photoeditor

import androidx.lifecycle.ViewModel
import com.tsovedenski.galleryonsteroids.common.mutableLiveData
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import java.io.File

/**
 * Created by Tsvetan Ovedenski on 20/04/2019.
 */
class PhotoEditorViewModel : ViewModel(), PhotoEditorContract.ViewModel {
    override var media: Media? by mutableLiveData()
    override var tempFile: File? by mutableLiveData()
    override var loaded: Boolean by mutableLiveData(false)
}