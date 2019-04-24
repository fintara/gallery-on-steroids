package com.tsovedenski.galleryonsteroids.features.medialist

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.tsovedenski.galleryonsteroids.common.mutableLiveData

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class MediaListViewModel (
    private val sharedPreferences: SharedPreferences
) : ViewModel(),
    MediaListContract.ViewModel {
    override var isLoaded: Boolean by mutableLiveData(false)
    override var viewType: ViewType by mutableLiveData(
        initializer = { ViewType.fromInt(sharedPreferences.getInt("viewType", 1)) ?: ViewType.Grid },
        onSetValue = { sharedPreferences.edit().putInt("viewType", it.asInt).apply() }
    )
}