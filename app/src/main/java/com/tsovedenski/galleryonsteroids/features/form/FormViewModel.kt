package com.tsovedenski.galleryonsteroids.features.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsovedenski.galleryonsteroids.domain.entities.Media

class FormViewModel (
    private val media: MutableLiveData<Media> = MutableLiveData()
) : ViewModel(), FormContract.ViewModel {

    override fun getMedia(): Media? = media.value

    override fun setMedia(value: Media?) {
        media.value = value
    }
}