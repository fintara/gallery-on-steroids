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


/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class CreatorVoiceFragment : Fragment(), CreatorMode {

    override var onRecordingFinished: ((Media) -> Unit)? = null

    private val handler by lazy { Handler() }

    private val media: Media = Media(type = MediaType.Audio)

    private val recorder by lazy {
        MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(media.path)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        }
    }

    private var time = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mode_voice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        waveform.visibility = View.INVISIBLE
    }

    override fun startRecording() {
        recorder.prepare()
        recorder.start()
        stopwatchUpdate()
    }

    override fun stopRecording() {
        handler.removeCallbacksAndMessages(null)
        recorder.stop()
        recorder.release()
//        onRecordingFinished?.let { it(media) }
    }

    private fun stopwatchUpdate() {
        stopwatch.text = time.toDurationString()
        time += STOPWATCH_UPDATE_RATE
        handler.postDelayed(::stopwatchUpdate, STOPWATCH_UPDATE_RATE)
    }

    companion object {
        private const val STOPWATCH_UPDATE_RATE = 50L
    }

//    private fun onAudioBuffer(buffer: ShortArray) {
//        waveform?.updateAudioData(buffer)
//
//        val byteBuffer = ByteBuffer.allocate(buffer.size * 2)
//
//        buffer
//            .map(::shortToByteArray)
//            .onEach { byteBuffer.put(it) }
//
//        val bytes = byteBuffer.array()
//
//        encoder.encode(ByteArrayInputStream(bytes), samplingRate)
//    }

//    private fun shortToByteArray(data: Short): ByteArray {
//        return byteArrayOf((data and 0xff).toByte(), ((data ushr 8) and 0xff).toByte())
//    }
//
//    private infix fun Short.ushr(other: Short): Short = (this.toInt() ushr other.toInt()).toShort()
}
