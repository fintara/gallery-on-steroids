package com.tsovedenski.galleryonsteroids.features.form

import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.features.common.Presenter
import com.tsovedenski.galleryonsteroids.services.MediaService
import kotlinx.coroutines.launch
import timber.log.Timber

class FormPresenter (
    private val view: FormContract.View,
    private val model: FormContract.ViewModel,
    private val service: MediaService,
    private val contextProvider: CoroutineContextProvider
): Presenter<FormEvent>(contextProvider) {

    init {
        Timber.tag(FormPresenter::class.java.name)
    }

    override fun onChanged(e: FormEvent) = when (e) {
        is FormEvent.OnStart -> onStart(e.media)
        FormEvent.OnResume -> onResume()
        FormEvent.OnDestroy -> onDestroy()
        is FormEvent.Save -> save(e)
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

    private fun save(data: FormEvent.Save) {
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