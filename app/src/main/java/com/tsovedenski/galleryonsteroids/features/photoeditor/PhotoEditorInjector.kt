package com.tsovedenski.galleryonsteroids.features.photoeditor

import androidx.lifecycle.ViewModelProviders
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider

/**
 * Created by Tsvetan Ovedenski on 20/04/2019.
 */
class PhotoEditorInjector (
    private val coroutineContextProvider: CoroutineContextProvider
) {

    fun attachPresenter(view: PhotoEditorView) {
        val presenter = PhotoEditorPresenter(
            view,
            ViewModelProviders.of(view).get(PhotoEditorViewModel::class.java),
            coroutineContextProvider
        )
        view.setObserver(presenter)
    }
}