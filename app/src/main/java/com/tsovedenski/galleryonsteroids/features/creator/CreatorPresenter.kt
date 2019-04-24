package com.tsovedenski.galleryonsteroids.features.creator

import android.Manifest
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.common.Presenter
import timber.log.Timber

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class CreatorPresenter (
    private val view: CreatorContract.View,
    private val model: CreatorContract.ViewModel,
    contextProvider: CoroutineContextProvider
) : Presenter<CreatorEvent>(contextProvider) {

    init {
        Timber.tag(CreatorPresenter::class.java.simpleName)
    }

    override fun onChanged(e: CreatorEvent) {
        Timber.i("Received event: $e")
        when (e) {
            is CreatorEvent.OnStart -> onStart(e.initialMediaType)
            CreatorEvent.OnResume -> onResume()
            CreatorEvent.OnDestroy -> onDestroy()
            CreatorEvent.RecordPressed -> recordPressed()
            is CreatorEvent.ChangeType -> onChangeType(e.value)
            is CreatorEvent.RecordedMedia -> recorded(e.value)
        }
    }

    private fun onStart(initialMediaType: MediaType) {
        if (model.mediaType == null) {
            model.mediaType = initialMediaType
        }
    }

    private fun onResume() {
        model.mediaType?.let(::onChangeType)
    }

    private fun recordPressed() {
        when (model.recordingState.also { Timber.i("Recording state: $it") }) {
            RecordingState.Idle -> startRecording()
            RecordingState.Recording -> stopRecording()
            RecordingState.Finishing -> Unit
        }
    }

    private fun startRecording() {
        view.startRecording()
        model.recordingState = RecordingState.Recording
        model.mediaType?.let { type ->
            if (type != MediaType.Photo) {
                view.startStopwatch()
            } else {
                stopRecording()
            }
        }
    }

    private fun stopRecording() {
        model.recordingState = RecordingState.Finishing
        view.stopRecording()
    }

    private fun recorded(media: Media) {
//        if (model.recordingState != RecordingState.Finishing) {
//            return
//        }

        when (media.type) {
            MediaType.Photo -> {
                Timber.i("Opening photo editor for ${media.id}")
                view.openPhotoEditor(media)
            }

            MediaType.Video,
            MediaType.Audio -> {
                Timber.i("Opening details for ${media.id}")
                view.openDetails(media)
            }
        }

        model.recordingState = RecordingState.Idle
    }

    private fun onChangeType(type: MediaType) {
        view.checkPermissions(R.string.need_write_permission, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (type == MediaType.Audio) {
            view.checkPermissions(R.string.need_audio_permission, Manifest.permission.RECORD_AUDIO)
        }

        model.mediaType = type
        view.setMediaType(type)
    }
}