package com.tsovedenski.galleryonsteroids.features.creator.modes

import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 31/03/19.
 */
interface CreatorMode {
    var onRecordingFinished: ((Media) -> Unit)?

    fun startRecording()
    fun stopRecording()
}