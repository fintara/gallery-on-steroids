package com.tsovedenski.galleryonsteroids.features.medialist

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class MediaListViewModel (
    private val sharedPreferences: SharedPreferences
) : ViewModel(),
    MediaListContract.ViewModel {

    private val loaded: MutableLiveData<Boolean> = MutableLiveData()
    private val viewType: MutableLiveData<ViewType> = MutableLiveData()

    init {
        loaded.value = false
        viewType.value = ViewType.fromInt(sharedPreferences.getInt("viewType", 1)) ?: ViewType.Grid
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
        sharedPreferences.edit().putInt("viewType", value.asInt).apply()
        viewType.value = value
    }
}