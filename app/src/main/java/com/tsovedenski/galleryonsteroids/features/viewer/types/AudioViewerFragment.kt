package com.tsovedenski.galleryonsteroids.features.viewer.types

import android.media.MediaPlayer
import android.net.Uri
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
import com.tsovedenski.galleryonsteroids.showToast
import kotlinx.android.synthetic.main.fragment_viewer_videoaudio.*
import timber.log.Timber
import java.io.File

/**
 * Created by Tsvetan Ovedenski on 07/04/19.
 */
class AudioViewerFragment : ViewerFragment() {

    private val seekbarListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            event.value = ViewerTypeEvent.ProgressChanged(progress, fromUser)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            event.value = ViewerTypeEvent.SeekStarted
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            event.value = ViewerTypeEvent.SeekEnded
            showControls()
        }
    }

    private val progressUiHandler by lazy { Handler() }

    private lateinit var player: MediaPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_viewer_videoaudio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        video_view.visibility = View.GONE

        playpause.setOnClickListener {
            event.value = ViewerTypeEvent.TogglePlaying
        }

        replay.setOnClickListener {
            event.value = ViewerTypeEvent.Replay
        }

        seekbar.setOnSeekBarChangeListener(seekbarListener)
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

    override fun prepare(media: Media) {
        val file = File(media.path)
        if (!file.exists()) {
            showToast("File does not exist")
            requireActivity().finish()
            return
        }
        player = MediaPlayer.create(requireContext(), Uri.fromFile(file))

        player.setOnCompletionListener {
            event.value = ViewerTypeEvent.PlayingCompleted
        }

        media_title.text = media.title
        seekbar.max = media.duration?.toInt() ?: 0
        duration_total.text = media.duration.toDurationString()
    }

    override fun play() {
        Timber.i("Start playing")
        progressUiHandler.removeCallbacksAndMessages(null)
        progressUiHandler.postDelayed(::checkTime, SEEKBAR_REFRESH_RATE)
        playpause.setImageResource(R.drawable.pause)

        player.start()
    }

    override fun pause() {
        Timber.i("Pause playing")
        playpause.setImageResource(R.drawable.play)
        progressUiHandler.removeCallbacksAndMessages(null)

        player.pause()
    }

    override fun seek(msec: Int, force: Boolean) {
        Timber.i("Seek to $msec ($force)")
        if (force) {
            player.seekTo(msec)
        }
        seekbar.progress = msec
        duration_current.text = msec.toLong().toDurationString()
    }

    override fun showControls() {
        media_controls?.animate()?.alpha(1f)?.start()
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

    private fun checkTime() {
        if (video_view != null) {
            val position = player.currentPosition
            event.value = ViewerTypeEvent.ProgressChanged(position, false)
        }

        progressUiHandler.postDelayed(::checkTime, SEEKBAR_REFRESH_RATE)
    }

    companion object {
        private const val SEEKBAR_REFRESH_RATE = 250L // ms

        fun newInstance(media: Media) = AudioViewerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ViewerView.INTENT_EXTRA_MEDIA, media)
            }
        }
    }
}