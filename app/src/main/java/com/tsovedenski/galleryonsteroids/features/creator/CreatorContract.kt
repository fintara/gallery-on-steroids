package com.tsovedenski.galleryonsteroids.features.creator

import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
interface CreatorContract {
    interface View {
        fun setObserver(observer: Observer<CreatorEvent>)
        fun setMediaType(value: MediaType)

        fun startRecording()
        fun stopRecording()
        fun startStopwatch()

        fun openDetails(media: Media)
        fun openPhotoEditor(media: Media)

        fun checkPermissions(@StringRes rationaleResId: Int, vararg perms: String)
    }

    interface ViewModel {
        var mediaType: MediaType?
        var recordingState: RecordingState
    }
}

sealed class RecordingState {
    object Idle : RecordingState() {
        override fun toString() = "Idle"
    }
    object Recording : RecordingState() {
        override fun toString() = "Recording"
    }
    object Finishing : RecordingState() {
        override fun toString() = "Finishing"
    }
}