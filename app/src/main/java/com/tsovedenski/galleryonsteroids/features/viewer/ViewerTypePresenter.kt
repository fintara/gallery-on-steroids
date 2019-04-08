package com.tsovedenski.galleryonsteroids.features.viewer

import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.common.Presenter
import kotlinx.coroutines.launch
import timber.log.Timber

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
        ViewerTypeEvent.MediaClicked -> mediaClicked()

        ViewerTypeEvent.SeekStarted -> seekStarted()
        ViewerTypeEvent.SeekEnded -> seekEnded()
        ViewerTypeEvent.TogglePlaying -> togglePlaying()
        ViewerTypeEvent.Replay -> replay()
        ViewerTypeEvent.PlayingCompleted -> playingCompleted()
        is ViewerTypeEvent.ProgressChanged -> progressChanged(e.value, e.force)
    }

    private fun onStart(media: Media) {
        model.setMedia(media)
        view.prepare(media)
        if (media.type != MediaType.Photo) {
            progressChanged(model.getProgress() + 1, true)
        }
    }

    private fun onResume() {
        model.getMedia()?.let { media ->
            Timber.i("onResume(media=${media.id})")
            view.prepare(media)

            if (media.type == MediaType.Video) {
                view.seek(model.getProgress(), true)
                when (model.getPlayingState()) {
                    PlayingState.Playing -> view.play()
                    PlayingState.Paused -> view.pause()
                    PlayingState.Completed -> playingCompleted()
                }
            }
        }
        showControls(model.isControlShown())
    }

    private fun mediaClicked() {
        val shown = model.isControlShown()
        showControls(!shown)
        model.setControlShown(!shown)
    }

    private fun showControls(show: Boolean) {
        if (show) {
            view.showControls()
        } else {
            view.hideControls()
        }
    }

    private fun togglePlaying() = when (model.getPlayingState()) {
        PlayingState.Playing -> pausePlaying()
        PlayingState.Paused -> startPlaying()
        PlayingState.Completed -> replay()
    }

    private fun startPlaying() {
        view.hideReplayButton()
        view.play()
        model.setPlayingState(PlayingState.Playing)
    }

    private fun pausePlaying() {
        view.hideReplayButton()
        view.pause()
        model.setPlayingState(PlayingState.Paused)
    }

    private fun seekStarted() {
        // if playing, pause
        // if paused, noop
        view.pause()
    }

    private fun seekEnded() {
        // if before seek was playing, play
        // if before seek was paused, noop
        when (model.getPlayingState()) {
            PlayingState.Playing -> {
                view.play()
            }

            PlayingState.Completed -> {
                view.hideReplayButton()
                pausePlaying()
            }
        }
    }

    private fun replay() {
        progressChanged(0, true)
        view.hideReplayButton()
        startPlaying()
    }

    private fun playingCompleted() {
        if (model.getPlayingState() == PlayingState.Playing) {
            view.showReplayButton()
            model.setPlayingState(PlayingState.Completed)
        }
    }

    private fun progressChanged(value: Int, force: Boolean) {
        view.seek(value, force)
        model.setProgress(value)
    }
}