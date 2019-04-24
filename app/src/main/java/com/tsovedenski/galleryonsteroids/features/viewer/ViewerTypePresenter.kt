package com.tsovedenski.galleryonsteroids.features.viewer

import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.common.Try
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.common.Presenter
import com.tsovedenski.galleryonsteroids.services.ImageLabeler
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
class ViewerTypePresenter (
    private val view: ViewerTypeContract.View,
    private val model: ViewerTypeContract.ViewModel,
    private val imageLabeler: ImageLabeler,
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
        model.media = media
        view.prepare(media)
        if (media.type != MediaType.Photo) {
            progressChanged(model.progress + 1, true)
        }
    }

    private fun onResume() {
        model.media?.let { media ->
            Timber.i("onResume(media=${media.id})")
            view.prepare(media)

            if (media.type == MediaType.Video) {
                view.seek(model.progress, true)
                when (model.playingState) {
                    PlayingState.Playing -> view.play()
                    PlayingState.Paused -> view.pause()
                    PlayingState.Completed -> playingCompleted()
                }
            }
            if (media.type == MediaType.Photo) {
                launch {
                    view.showLabelSpinner()
                    val list = Try { imageLabeler.label("file://${media.path}") }
                    view.hideLabelSpinner()
                    list.fold(
                        left = {
                            Timber.e(it)
                            view.setEmptyLabels(it.message ?: "Unknown error")
                        },
                        right = {
                            if (it.isEmpty()) {
                                view.setEmptyLabels(R.string.no_labels)
                            } else {
                                view.setLabels(it.take(3))
                            }
                        }
                    )
                }
            }
        }
        showControls(model.isControlShown)
    }

    private fun mediaClicked() {
        val shown = model.isControlShown
        showControls(!shown)
        model.isControlShown = !shown
    }

    private fun showControls(show: Boolean) {
        if (show) {
            view.showControls()
        } else {
            view.hideControls()
        }
    }

    private fun togglePlaying() = when (model.playingState) {
        PlayingState.Playing -> pausePlaying()
        PlayingState.Paused -> startPlaying()
        PlayingState.Completed -> replay()
    }

    private fun startPlaying() {
        view.hideReplayButton()
        view.play()
        model.playingState = PlayingState.Playing
    }

    private fun pausePlaying() {
        view.hideReplayButton()
        view.pause()
        model.playingState = PlayingState.Paused
    }

    private fun seekStarted() {
        // if playing, pause
        // if paused, noop
        view.pause()
    }

    private fun seekEnded() {
        // if before seek was playing, play
        // if before seek was paused, noop
        when (model.playingState) {
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
        if (model.playingState == PlayingState.Playing) {
            view.showReplayButton()
            model.playingState = PlayingState.Completed
        }
    }

    private fun progressChanged(value: Int, force: Boolean) {
        view.seek(value, force)
        model.progress = value
    }
}