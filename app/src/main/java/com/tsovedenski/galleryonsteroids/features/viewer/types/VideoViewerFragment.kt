package com.tsovedenski.galleryonsteroids.features.viewer.types

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.common.toDurationString
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerActivity
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerFragment
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerTypeContract
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerTypeEvent
import kotlinx.android.synthetic.main.fragment_viewer_video.*

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
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            event.value = ViewerTypeEvent.SeekEnded
        }
    }

    private val handler by lazy { Handler() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_viewer_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playpause.setOnClickListener {
            event.value = ViewerTypeEvent.TogglePlaying
        }

        video_view.setOnPreparedListener {
            handler.postDelayed(::checkVideoTime, 100)
        }

        seekbar.setOnSeekBarChangeListener(seekbarListener)
    }

    override fun prepare(media: Media) {
        video_view.setVideoPath(media.path)
        seekbar.max = media.duration?.toInt() ?: 0
        duration_total.text = media.duration.toDurationString()
    }

    override fun play() {
        playpause.setImageResource(R.drawable.pause)
        video_view.start()
    }

    override fun pause() {
        playpause.setImageResource(R.drawable.play)
        video_view.pause()
    }

    override fun seek(msec: Int, force: Boolean) {
        if (force) {
            video_view.seekTo(msec)
        }
        seekbar.progress = msec
        duration_current.text = msec.toLong().toDurationString()
    }

    private fun checkVideoTime() {
        if (video_view != null) {
            val position = video_view.currentPosition
            event.value = ViewerTypeEvent.ProgressChanged(position, false)
        }

        handler.postDelayed(::checkVideoTime, 200)
    }

    companion object {
        fun newInstance(media: Media) = VideoViewerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ViewerActivity.INTENT_EXTRA_MEDIA, media)
            }
        }
    }
}