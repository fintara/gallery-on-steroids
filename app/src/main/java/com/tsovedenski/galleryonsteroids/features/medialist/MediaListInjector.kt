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
    private val context: Context,
    private val mediaService: MediaService,
    private val coroutineContextProvider: CoroutineContextProvider
) {
    fun attachPresenter(view: MediaListActivity) {
        val presenter = MediaListPresenter(
            view,
            ViewModelProviders.of(view).get(MediaListViewModel::class.java),
            mediaService,
            MediaListAdapter(context),
            coroutineContextProvider
        )
        view.setObserver(presenter)
    }
}