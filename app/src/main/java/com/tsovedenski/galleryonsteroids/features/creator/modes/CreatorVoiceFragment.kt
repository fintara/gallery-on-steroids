package com.tsovedenski.galleryonsteroids.features.creator.modes

import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.common.toDurationString
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import kotlinx.android.synthetic.main.fragment_mode_voice.*
import timber.log.Timber
import java.util.*


/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class CreatorVoiceFragment : Fragment(), CreatorMode {

    override var onRecordingFinished: ((Media) -> Unit)? = null

    private val media: Media = Media(type = MediaType.Audio)

    private val recorder by lazy {
        MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(media.path)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        }
    }

    private val handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.tag(CreatorVoiceFragment::class.java.name)
        return inflater.inflate(R.layout.fragment_mode_voice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        waveform.apply {
            visibility = View.INVISIBLE
            background = resources.getColor(R.color.black, requireActivity().theme)
            barBackground = resources.getColor(R.color.colorPrimary, requireActivity().theme)
            bars = 88
            spacing = 3
        }
    }

    override fun startRecording() {
        waveform.visibility = View.VISIBLE
        recorder.prepare()
        recorder.start()
        updateWave()
    }

    override fun stopRecording() {
        recorder.stop()
        recorder.release()
        handler.removeCallbacksAndMessages(null)
        onRecordingFinished?.let { it(media) }
    }

    private fun updateWave() {
        waveform.addAmplitude(recorder.maxAmplitude.also { Timber.i("Amp = $it") })
        handler.postDelayed(::updateWave, 30)
    }
}
