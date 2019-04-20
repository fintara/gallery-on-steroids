package com.tsovedenski.galleryonsteroids.features.photoeditor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 20/04/2019.
 */
class PhotoEditorViewModel : ViewModel(), PhotoEditorContract.ViewModel {

    private val _media = MutableLiveData<Media>()

    override var media: Media?
        get() = _media.value
        set(value) { _media.value = value }
}