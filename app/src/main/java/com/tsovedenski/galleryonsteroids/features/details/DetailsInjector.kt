package com.tsovedenski.galleryonsteroids.features.details

import androidx.lifecycle.ViewModelProviders
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.services.MediaService

class DetailsInjector (
    private val mediaService: MediaService,
    private val coroutineContextProvider: CoroutineContextProvider
) {
    fun attachPresenter(view: DetailsActivity) {
        val presenter = DetailsPresenter(
            view,
            ViewModelProviders.of(view).get(DetailsViewModel::class.java),
            mediaService,
            coroutineContextProvider
        )
        view.setObserver(presenter)
    }
}