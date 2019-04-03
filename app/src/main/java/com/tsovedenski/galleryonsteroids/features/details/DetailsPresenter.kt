package com.tsovedenski.galleryonsteroids.features.details

import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.features.common.Presenter
import com.tsovedenski.galleryonsteroids.services.MediaService
import kotlinx.coroutines.launch

class DetailsPresenter (
    private val view: DetailsContract.View,
    private val model: DetailsContract.ViewModel,
    private val service: MediaService,
    private val contextProvider: CoroutineContextProvider
): Presenter<DetailsEvent>(contextProvider) {

    override fun onChanged(e: DetailsEvent) = when (e) {
        is DetailsEvent.OnStart -> onStart(e.media)
        DetailsEvent.OnResume -> onResume()
        DetailsEvent.OnDestroy -> onDestroy()
        is DetailsEvent.Save -> save(e)
    }

    private fun onStart(media: Media) {
        if (model.getMedia() == null) {
            model.setMedia(media)
        }
    }

    private fun onResume() {

    }

    private fun save(data: DetailsEvent.Save) {
        // todo: validate
        val media = model.getMedia()!!.copy(
            title = data.title
        )

        launch { service.save(media) }
    }
}