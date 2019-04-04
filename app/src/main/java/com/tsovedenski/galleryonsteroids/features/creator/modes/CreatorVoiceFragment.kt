package com.tsovedenski.galleryonsteroids.features.creator.modes

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.semantive.pcm_encoder.PCMEncoder
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import kotlinx.android.synthetic.main.fragment_mode_voice.*
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import kotlin.concurrent.thread
import kotlin.experimental.and


/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class CreatorVoiceFragment : Fragment(), CreatorMode {

    override var onRecordingFinished: ((Media) -> Unit)? = null

    private val sampleRate = 44_100
    private val channels = 2
    private val bitrate = 224

    private val previewThread: Thread = thread(start = false) {
        var bufferSize = AudioRecord.getMinBufferSize(sampleRate,
            if (channels == 1) AudioFormat.CHANNEL_IN_MONO else AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT)

        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = sampleRate * 2
        }

        val audioBuffer = ShortArray(bufferSize / 2)

        val audio = AudioRecord(
            MediaRecorder.AudioSource.DEFAULT,
            sampleRate,
            if (channels == 1) AudioFormat.CHANNEL_IN_MONO else AudioFormat.CHANNEL_IN_STEREO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        if (audio.state != AudioRecord.STATE_INITIALIZED) {
            Timber.e("Audio Record can't initialize!")
            return@thread
        }

        try {
            audio.startRecording()

            var shortsRead: Long = 0
            while (!Thread.interrupted()) {
                val numberOfShort = audio.read(audioBuffer, 0, audioBuffer.size)
                shortsRead += numberOfShort
                onAudioBuffer(audioBuffer)
            }
        } catch (e: Exception) {
            Timber.e(e)
        } finally {
            audio.stop()
            audio.release()
        }
    }

    private val media: Media = Media(type = MediaType.Audio)

    private val encoder by lazy { PCMEncoder(bitrate, sampleRate, channels).apply {
        setOutputPath(media.path)
    } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mode_voice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        waveform.visibility = View.INVISIBLE
    }

    private fun startPreview() {
        previewThread.start()
        waveform.visibility = View.VISIBLE
    }

    private fun stopPreview() {
        previewThread.interrupt()
    }

    override fun startRecording() {
        encoder.prepare()
        startPreview()
    }

    override fun stopRecording() {
        encoder.stop()
        stopPreview()
        onRecordingFinished?.let { it(media) }
    }

    private fun onAudioBuffer(buffer: ShortArray) {
        waveform?.updateAudioData(buffer)

        val byteBuffer = ByteBuffer.allocate(buffer.size * 2)

        buffer
            .map(::shortToByteArray)
            .onEach { byteBuffer.put(it) }

        val bytes = byteBuffer.array()

        encoder.encode(ByteArrayInputStream(bytes), sampleRate)
    }

    private fun shortToByteArray(data: Short): ByteArray {
        return byteArrayOf((data and 0xff).toByte(), ((data ushr 8) and 0xff).toByte())
    }

    private infix fun Short.ushr(other: Short): Short = (this.toInt() ushr other.toInt()).toShort()
}
