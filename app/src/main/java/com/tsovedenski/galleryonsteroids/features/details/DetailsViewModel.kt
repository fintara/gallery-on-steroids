package com.tsovedenski.galleryonsteroids.features.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsovedenski.galleryonsteroids.domain.entities.Media

class DetailsViewModel (
    private val media: MutableLiveData<Media> = MutableLiveData()
) : ViewModel(), DetailsContract.ViewModel {

    override fun getMedia(): Media? = media.value

    override fun setMedia(value: Media) {
        media.value = value
    }
}