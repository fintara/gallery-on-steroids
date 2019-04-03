package com.tsovedenski.galleryonsteroids.features.medialist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class MediaListViewModel (
    private val loaded: MutableLiveData<Boolean> = MutableLiveData(),
    private val viewType: MutableLiveData<ViewType> = MutableLiveData()
) : ViewModel(),
    MediaListContract.ViewModel {

    init {
        loaded.value = false
        viewType.value = ViewType.Grid
    }

    override fun isLoaded(): Boolean {
        return loaded.value!!
    }

    override fun setLoaded(value: Boolean) {
        loaded.value = value
    }

    override fun getViewType(): ViewType {
        return viewType.value!!
    }

    override fun setViewType(value: ViewType) {
        viewType.value = value
    }
}