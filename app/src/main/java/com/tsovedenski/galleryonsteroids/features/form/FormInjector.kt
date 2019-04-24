package com.tsovedenski.galleryonsteroids.features.form

import androidx.lifecycle.ViewModelProviders
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.services.ImageLabeler
import com.tsovedenski.galleryonsteroids.services.MediaService

class FormInjector (
    private val mediaService: MediaService,
    private val imageLabeler: ImageLabeler,
    private val coroutineContextProvider: CoroutineContextProvider
) {
    fun attachPresenter(view: FormView) {
        val presenter = FormPresenter(
            view,
            ViewModelProviders.of(view).get(FormViewModel::class.java),
            mediaService,
            imageLabeler,
            coroutineContextProvider
        )
        view.setObserver(presenter)
    }
}