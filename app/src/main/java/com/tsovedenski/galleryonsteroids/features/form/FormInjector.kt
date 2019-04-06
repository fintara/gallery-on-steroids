package com.tsovedenski.galleryonsteroids.features.form

import androidx.lifecycle.ViewModelProviders
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.services.MediaService

class FormInjector (
    private val mediaService: MediaService,
    private val coroutineContextProvider: CoroutineContextProvider
) {
    fun attachPresenter(view: FormActivity) {
        val presenter = FormPresenter(
            view,
            ViewModelProviders.of(view).get(FormViewModel::class.java),
            mediaService,
            coroutineContextProvider
        )
        view.setObserver(presenter)
    }
}