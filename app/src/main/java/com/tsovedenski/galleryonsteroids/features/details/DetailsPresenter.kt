package com.tsovedenski.galleryonsteroids.features.details

import android.media.ThumbnailUtils
import android.provider.MediaStore
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.features.common.Presenter
import com.tsovedenski.galleryonsteroids.services.MediaService
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class DetailsPresenter (
    private val view: DetailsContract.View,
    private val model: DetailsContract.ViewModel,
    private val service: MediaService,
    private val contextProvider: CoroutineContextProvider
): Presenter<DetailsEvent>(contextProvider) {

    init {
        Timber.tag(DetailsPresenter::class.java.name)
    }

    override fun onChanged(e: DetailsEvent) = when (e) {
        is DetailsEvent.OnStart -> onStart(e.media)
        DetailsEvent.OnResume -> onResume()
        DetailsEvent.OnDestroy -> onDestroy()
        is DetailsEvent.Save -> save(e)
    }

    private fun onStart(media: Media) {
        if (model.getMedia() == null) {
            model.setMedia(media)
            view.setThumbnail(media)
        }
    }

    private fun onResume() {
        model.getMedia()?.let { media ->
            Timber.i(media.toString())
            view.setThumbnail(media)
        }
    }

    private fun save(data: DetailsEvent.Save) {
        // todo: validate
        model.getMedia()?.let { media ->
            val toSave = media.copy(
                title = data.title
            )

            launch {
                service.save(toSave)
                view.close()
                model.setMedia(null)
            }
        }
    }
}