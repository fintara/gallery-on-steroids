package com.tsovedenski.galleryonsteroids.features.form

import androidx.lifecycle.ViewModel
import com.tsovedenski.galleryonsteroids.common.mutableLiveData
import com.tsovedenski.galleryonsteroids.domain.entities.Media

class FormViewModel : ViewModel(), FormContract.ViewModel {
    override var media: Media? by mutableLiveData()
}