package com.tsovedenski.galleryonsteroids.features.viewer

import androidx.lifecycle.ViewModelProviders
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.features.viewer.types.ViewerFragment
import com.tsovedenski.galleryonsteroids.services.ImageLabeler

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
class ViewerTypeInjector (
    private val imageLabeler: ImageLabeler,
    private val coroutineContextProvider: CoroutineContextProvider
) {
    fun attachPresenter(view: ViewerFragment) {
        val presenter = ViewerTypePresenter(
            view,
            ViewModelProviders.of(view).get(ViewerTypeViewModel::class.java),
            imageLabeler,
            coroutineContextProvider
        )
        view.setObserver(presenter)
    }
}