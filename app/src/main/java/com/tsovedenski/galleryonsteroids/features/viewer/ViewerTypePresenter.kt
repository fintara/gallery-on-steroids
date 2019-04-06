package com.tsovedenski.galleryonsteroids.features.viewer

import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.features.common.Presenter

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
class ViewerTypePresenter (
    private val view: ViewerTypeContract.View,
    private val model: ViewerTypeContract.ViewModel,
    coroutineContextProvider: CoroutineContextProvider
) : Presenter<ViewerTypeEvent>(coroutineContextProvider) {
    override fun onChanged(e: ViewerTypeEvent) = when (e) {
        is ViewerTypeEvent.OnStart -> onStart(e.media)
        ViewerTypeEvent.OnResume -> onResume()
        ViewerTypeEvent.OnDestroy -> onDestroy()

        ViewerTypeEvent.SeekStarted -> seekStarted()
        ViewerTypeEvent.SeekEnded -> seekEnded()
        ViewerTypeEvent.TogglePlaying -> togglePlaying()
        ViewerTypeEvent.Replay -> replay()
        is ViewerTypeEvent.ProgressChanged -> progressChanged(e.value, e.force)
    }

    private fun onStart(media: Media) {
        view.prepare(media)
        progressChanged(model.getProgress()+1, true)
    }

    private fun onResume() {
        view.seek(model.getProgress(), true)
        if (model.isPlaying()) {
            view.play()
        } else {
            view.pause()
        }
    }

    private fun togglePlaying() = when (model.isPlaying()) {
        true -> pausePlaying()
        else -> startPlaying()
    }

    private fun startPlaying() {
        view.play()
        model.setPlaying(true)
    }

    private fun pausePlaying() {
        view.pause()
        model.setPlaying(false)
    }

    private fun seekStarted() {
        // if playing, pause
        // if paused, noop
        view.pause()
    }

    private fun seekEnded() {
        // if before seek was playing, play
        // if before seek was paused, noop
        if (model.isPlaying()) {
            view.play()
        }
    }

    private fun replay() {
        progressChanged(0, true)
        startPlaying()
    }

    private fun progressChanged(value: Int, force: Boolean) {
        view.seek(value, force)
        model.setProgress(value)
    }
}