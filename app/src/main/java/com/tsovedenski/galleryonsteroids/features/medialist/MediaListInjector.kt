package com.tsovedenski.galleryonsteroids.features.medialist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.services.MediaService

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class MediaListInjector (
    private val context: Context,
    private val mediaService: MediaService,
    private val coroutineContextProvider: CoroutineContextProvider
) {
    @Suppress("UNCHECKED_CAST")
    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MediaListViewModel(context.getSharedPreferences(MediaListPresenter::javaClass.name, Context.MODE_PRIVATE)) as T
        }
    }

    fun attachPresenter(view: MediaListView) {
        val presenter = MediaListPresenter(
            view,
            ViewModelProviders.of(view, viewModelFactory).get(MediaListViewModel::class.java),
            mediaService,
            MediaListAdapter(context),
            coroutineContextProvider
        )
        view.setObserver(presenter)
    }
}