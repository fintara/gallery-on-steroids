package com.tsovedenski.galleryonsteroids.features.viewer

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
class ViewerTypeInjector (
    private val coroutineContextProvider: CoroutineContextProvider
) {
    fun attachPresenter(view: ViewerFragment) {
        val presenter = ViewerTypePresenter(
            view,
            ViewModelProviders.of(view).get(ViewerTypeViewModel::class.java),
            coroutineContextProvider
        )
        view.setObserver(presenter)
    }
}