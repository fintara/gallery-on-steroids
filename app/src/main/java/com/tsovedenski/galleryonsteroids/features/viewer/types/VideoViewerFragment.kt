package com.tsovedenski.galleryonsteroids.features.viewer.types

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.common.toDurationString
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerTypeEvent
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerView
import kotlinx.android.synthetic.main.fragment_viewer_videoaudio.*

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
class VideoViewerFragment : ViewerFragment() {

    private val seekbarListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            event.value = ViewerTypeEvent.ProgressChanged(progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            event.value = ViewerTypeEvent.SeekStarted
            controlsUiHandler.removeCallbacksAndMessages(null)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            event.value = ViewerTypeEvent.SeekEnded
            showControls()
        }
    }

    private val progressUiHandler by lazy { Handler() }
    private val controlsUiHandler by lazy { Handler() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_viewer_videoaudio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playpause.setOnClickListener {
            event.value = ViewerTypeEvent.TogglePlaying
        }

        video_view.setOnClickListener {
            controlsUiHandler.removeCallbacksAndMessages(null)
            event.value = ViewerTypeEvent.MediaClicked
        }

        video_view.setOnCompletionListener {
            event.value = ViewerTypeEvent.PlayingCompleted
        }

        replay.setOnClickListener {
            event.value = ViewerTypeEvent.Replay
        }

        seekbar.setOnSeekBarChangeListener(seekbarListener)
    }

    override fun prepare(media: Media) {
        media_title.text = media.title
        video_view.setVideoPath(media.path)
        seekbar.max = media.duration?.toInt() ?: 0
        duration_total.text = media.duration.toDurationString()
    }

    override fun play() {
        progressUiHandler.removeCallbacksAndMessages(null)
        progressUiHandler.postDelayed(::checkVideoTime, SEEKBAR_REFRESH_RATE)
        playpause.setImageResource(R.drawable.pause)
        video_view.start()
    }

    override fun pause() {
        playpause.setImageResource(R.drawable.play)
        video_view.pause()
        progressUiHandler.removeCallbacksAndMessages(null)
    }

    override fun seek(msec: Int, force: Boolean) {
        if (force) {
            video_view.seekTo(msec)
        }
        seekbar.progress = msec
        duration_current.text = msec.toLong().toDurationString()
    }

    override fun showControls() {
        media_controls?.animate()?.alpha(1f)?.start()
        controlsUiHandler.postDelayed({ event.value = ViewerTypeEvent.MediaClicked }, CONTROLS_AUTOHIDE_DELAY)
    }

    override fun hideControls() {
        media_controls?.animate()?.alpha(0f)?.start()
    }

    override fun showReplayButton() {
        progressUiHandler.removeCallbacksAndMessages(null)
        playpause.visibility = View.INVISIBLE
        replay.visibility = View.VISIBLE
    }

    override fun hideReplayButton() {
        replay.visibility = View.GONE
        playpause.visibility = View.VISIBLE
    }

    private fun checkVideoTime() {
        if (video_view != null) {
            val position = video_view.currentPosition
            event.value = ViewerTypeEvent.ProgressChanged(position, false)
        }

        progressUiHandler.postDelayed(::checkVideoTime, SEEKBAR_REFRESH_RATE)
    }

    companion object {
        private const val SEEKBAR_REFRESH_RATE = 250L // ms
        private const val CONTROLS_AUTOHIDE_DELAY = 3500L // ms

        fun newInstance(media: Media) = VideoViewerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ViewerView.INTENT_EXTRA_MEDIA, media)
            }
        }
    }
}