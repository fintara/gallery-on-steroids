package com.tsovedenski.galleryonsteroids.features.viewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
class ViewerTypeViewModel (
    private val media: MutableLiveData<Media> = MutableLiveData(),
    private val playingState: MutableLiveData<PlayingState> = MutableLiveData(),
    private val progress: MutableLiveData<Int> = MutableLiveData(),
    private val controlShown: MutableLiveData<Boolean> = MutableLiveData()
) : ViewModel(),
    ViewerTypeContract.ViewModel {

    init {
        setPlayingState(PlayingState.Paused)
        setProgress(0)
        setControlShown(true)
    }

    override fun getMedia(): Media? = media.value

    override fun setMedia(value: Media?) {
        media.value = value
    }

    override fun getPlayingState(): PlayingState = playingState.value!!

    override fun setPlayingState(value: PlayingState) {
        playingState.value = value
    }

    override fun getProgress(): Int = progress.value!!

    override fun setProgress(value: Int) {
        progress.value = value
    }

    override fun isControlShown(): Boolean = controlShown.value!!

    override fun setControlShown(value: Boolean) {
        controlShown.value = value
    }
}