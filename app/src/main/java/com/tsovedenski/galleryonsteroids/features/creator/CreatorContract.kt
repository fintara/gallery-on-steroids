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

        // todo: not really part of view
        fun startRecording()
        fun stopRecording()

        fun openDetails(media: Media)

        fun checkPermissions(@StringRes rationaleResId: Int, vararg perms: String)
    }

    interface ViewModel {
        fun getMediaType(): MediaType?
        fun setMediaType(value: MediaType)

        fun getRecordingState(): RecordingState
        fun setRecordingState(value: RecordingState)
    }
}

sealed class RecordingState {
    object Idle : RecordingState()
    object Recording : RecordingState()
    object Finishing : RecordingState()
}