package com.tsovedenski.galleryonsteroids.features.creator

import androidx.lifecycle.ViewModelProviders
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class CreatorInjector (
    private val coroutineContextProvider: CoroutineContextProvider
) {
    fun attachPresenter(view: CreatorView) {
        val presenter = CreatorPresenter(
            view,
            ViewModelProviders.of(view).get(CreatorViewModel::class.java),
            coroutineContextProvider
        )
        view.setObserver(presenter)
    }
}