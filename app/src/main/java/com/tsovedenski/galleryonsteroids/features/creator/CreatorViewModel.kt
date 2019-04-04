package com.tsovedenski.galleryonsteroids.features.creator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class CreatorViewModel (
    private val mediaType: MutableLiveData<MediaType> = MutableLiveData(),
    private val recordingState: MutableLiveData<RecordingState> = MutableLiveData()
) : ViewModel(),
    CreatorContract.ViewModel {

    init {
        recordingState.value = RecordingState.Idle
    }

    override fun getMediaType(): MediaType? = mediaType.value

    override fun setMediaType(value: MediaType) {
        mediaType.value = value
    }

    override fun getRecordingState(): RecordingState = recordingState.value!!

    override fun setRecordingState(value: RecordingState) {
        recordingState.value = value
    }
}