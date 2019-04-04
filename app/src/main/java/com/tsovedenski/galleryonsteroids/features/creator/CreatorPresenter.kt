package com.tsovedenski.galleryonsteroids.features.creator

import android.Manifest
import com.tsovedenski.galleryonsteroids.R
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
        model.getMediaType()?.let(::onChangeType)
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
        model.setRecording(true)
    }

    private fun stopRecording() {
        view.stopRecording()
        model.setRecording(false)
    }

    private fun recorded(media: Media) {
        // save in database?
        // open details view?

//        if (media.type == MediaType.Video || media.type == MediaType.Audio) {
            view.openDetails(media)
//        }
    }

    private fun onChangeType(type: MediaType) {
        view.checkPermissions(R.string.need_write_permission, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (type == MediaType.Audio) {
            view.checkPermissions(R.string.need_audio_permission, Manifest.permission.RECORD_AUDIO)
        }

        model.setMediaType(type)
        view.setMediaType(type)
    }
}