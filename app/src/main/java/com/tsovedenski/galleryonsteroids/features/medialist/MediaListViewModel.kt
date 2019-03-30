package com.tsovedenski.galleryonsteroids.features.medialist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class MediaListViewModel (
    private val loaded: MutableLiveData<Boolean> = MutableLiveData()
) : ViewModel(),
    MediaListContract.ViewModel {

    init {
        loaded.value = false
    }

    override fun isLoaded(): Boolean {
        return loaded.value!!
    }

    override fun setLoaded(value: Boolean) {
        loaded.value = value
    }
}