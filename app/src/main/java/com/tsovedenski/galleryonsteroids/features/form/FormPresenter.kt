package com.tsovedenski.galleryonsteroids.features.form

import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.features.common.Presenter
import com.tsovedenski.galleryonsteroids.services.MediaService
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class FormPresenter (
    private val view: FormContract.View,
    private val model: FormContract.ViewModel,
    private val service: MediaService,
    contextProvider: CoroutineContextProvider
): Presenter<FormEvent>(contextProvider) {

    init {
        Timber.tag(FormPresenter::class.java.name)
    }

    override fun onChanged(e: FormEvent) = when (e) {
        is FormEvent.OnStart -> onStart(e.media)
        FormEvent.OnResume -> onResume()
        FormEvent.OnPause -> onPause()
        FormEvent.OnDestroy -> onDestroy()
        FormEvent.Discard -> discard()
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

    private fun onPause() {
        // save unsaved just in case
//        Timber.i("Saving media onPause")
//        save(FormEvent.Save("Unnamed"), removeFromModel = false)
    }

    private fun discard() {
        Timber.i("About to discard media")
        model.getMedia()?.let { media ->
            Timber.i("Discarding ${media.id}")
            launch {
                service.delete(media)
            }
        }
    }

    private fun save(data: FormEvent.Save, removeFromModel: Boolean = true) {
        if (!isTitleValid(data.title)) {
            view.showMessage(R.string.invalid_title)
            return
        }

        model.getMedia()?.let { media ->
            val toSave = media.copy(
                title = data.title
            )

            launch {
                service.save(toSave)
                view.close()
                if (removeFromModel) {
                    model.setMedia(null)
                }
            }
        }
    }

    private fun isTitleValid(value: String): Boolean = value.trim().matches(lettersNumbersSpace)

    companion object {
        private val lettersNumbersSpace = "[a-zA-Z0-9 ]{1,20}".toRegex()
    }
}