package com.tsovedenski.galleryonsteroids.features.medialist

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.repositories.MediaRepository
import com.tsovedenski.galleryonsteroids.services.MediaService

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class MediaListInjector (
    private val mediaService: MediaService,
    private val coroutineContextProvider: CoroutineContextProvider
) {
    fun attachPresenter(view: MediaListView): MediaListPresenter {
        val presenter = MediaListPresenter(
            view,
            ViewModelProviders.of(view).get(MediaListViewModel::class.java),
            mediaService,
            MediaListAdapter(),
            coroutineContextProvider
        )
        view.setObserver(presenter)
        return presenter
    }
}