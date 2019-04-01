package com.tsovedenski.galleryonsteroids.features.creator

import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.common.Presenter

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class CreatorPresenter (
    private val view: CreatorContract.View,
    private val model: CreatorContract.ViewModel,
    contextProvider: CoroutineContextProvider
) : Presenter<CreatorEvent>(contextProvider) {

    override fun onChanged(e: CreatorEvent) = when (e) {
        is CreatorEvent.OnStart -> onStart(e.initialMediaType)
        CreatorEvent.OnResume -> onResume()
        CreatorEvent.OnDestroy -> onDestroy()
        CreatorEvent.RecordPressed -> recordPressed()
        is CreatorEvent.ChangeType -> onChangeType(e.value)
        is CreatorEvent.RecordedMedia -> recorded(e.value)
    }

    private fun onStart(initialMediaType: MediaType) {
        if (model.getMediaType() == null) {
            model.setMediaType(initialMediaType)
        }
    }

    private fun onResume() {
        view.setMediaType(model.getMediaType()!!)
    }

    private fun recordPressed() {
        if (model.isRecording()) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    private fun startRecording() {
        view.startRecording()
    }

    private fun stopRecording() {
        view.stopRecording()
    }

    private fun recorded(media: Media) {
        // save in database?
        // open details view?
    }

    private fun onChangeType(type: MediaType) {
        when (type) {
            MediaType.Photo -> Unit
            MediaType.Video -> Unit
            MediaType.Audio -> Unit
        }

        model.setMediaType(type)
        view.setMediaType(type)
    }
}