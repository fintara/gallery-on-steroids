package com.tsovedenski.galleryonsteroids.features.photoeditor

import androidx.lifecycle.ViewModelProviders
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.services.MediaService

/**
 * Created by Tsvetan Ovedenski on 20/04/2019.
 */
class PhotoEditorInjector (
    private val service: MediaService,
    private val coroutineContextProvider: CoroutineContextProvider
) {

    fun attachPresenter(view: PhotoEditorView) {
        val presenter = PhotoEditorPresenter(
            view,
            ViewModelProviders.of(view).get(PhotoEditorViewModel::class.java),
            service,
            coroutineContextProvider
        )
        view.setObserver(presenter)
    }
}