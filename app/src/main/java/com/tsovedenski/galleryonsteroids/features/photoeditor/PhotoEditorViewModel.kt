package com.tsovedenski.galleryonsteroids.features.photoeditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import java.io.File

/**
 * Created by Tsvetan Ovedenski on 20/04/2019.
 */
class PhotoEditorViewModel : ViewModel(), PhotoEditorContract.ViewModel {

    private val _media = MutableLiveData<Media>()

    override var media: Media?
        get() = _media.value
        set(value) { _media.value = value }

    private val _tempFile = MutableLiveData<File>()

    override var tempFile: File?
        get() = _tempFile.value
        set(value) { _tempFile.value = value }

    private val _loaded = MutableLiveData<Boolean>()

    override var loaded: Boolean
        get() = _loaded.value ?: false
        set(value) { _loaded.value = value }
}