package com.tsovedenski.galleryonsteroids.features.viewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
class ViewerTypeViewModel (
    private val media: MutableLiveData<Media> = MutableLiveData(),
    private val playing: MutableLiveData<Boolean> = MutableLiveData(),
    private val progress: MutableLiveData<Int> = MutableLiveData()
) : ViewModel(),
    ViewerTypeContract.ViewModel {

    init {
        setPlaying(false)
        setProgress(0)
    }

    override fun getMedia(): Media? = media.value

    override fun setMedia(value: Media?) {
        media.value = value
    }

    override fun isPlaying(): Boolean = playing.value!!

    override fun setPlaying(value: Boolean) {
        playing.value = value
    }

    override fun getProgress(): Int = progress.value!!

    override fun setProgress(value: Int) {
        progress.value = value
    }
}