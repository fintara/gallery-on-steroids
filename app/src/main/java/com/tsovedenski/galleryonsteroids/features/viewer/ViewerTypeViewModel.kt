package com.tsovedenski.galleryonsteroids.features.viewer

import androidx.lifecycle.ViewModel
import com.tsovedenski.galleryonsteroids.common.mutableLiveData
import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
class ViewerTypeViewModel : ViewModel(), ViewerTypeContract.ViewModel {
    override var media: Media? by mutableLiveData()
    override var playingState: PlayingState by mutableLiveData(PlayingState.Paused)
    override var progress: Int by mutableLiveData(0)
    override var isControlShown: Boolean by mutableLiveData(true)
}