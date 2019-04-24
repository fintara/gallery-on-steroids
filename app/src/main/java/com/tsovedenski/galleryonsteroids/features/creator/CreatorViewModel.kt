package com.tsovedenski.galleryonsteroids.features.creator

import androidx.lifecycle.ViewModel
import com.tsovedenski.galleryonsteroids.common.mutableLiveData
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class CreatorViewModel : ViewModel(), CreatorContract.ViewModel {
    override var mediaType: MediaType? by mutableLiveData()
    override var recordingState: RecordingState by mutableLiveData(RecordingState.Idle)
}