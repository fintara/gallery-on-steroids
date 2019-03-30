package com.tsovedenski.galleryonsteroids.features.creator

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tsovedenski.galleryonsteroids.R
import kotlinx.android.synthetic.main.fragment_mode_voice.*
import kotlin.concurrent.thread

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class CreatorVoiceFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mode_voice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        thread {
            val sampleRate = 44_100

            var bufferSize = AudioRecord.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT)

            if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                bufferSize = sampleRate * 2;
            }

            val audioBuffer = ShortArray(bufferSize / 2)

            val audio = AudioRecord(
                MediaRecorder.AudioSource.DEFAULT,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            if (audio.getState() != AudioRecord.STATE_INITIALIZED) {
                Log.e("Audio", "audio Record can't initialize!");
            } else {
                audio.startRecording()

                var shortsRead: Long = 0
                while (shortsRead < 10_000_000) {
                    val numberOfShort = audio.read(audioBuffer, 0, audioBuffer.size)
                    shortsRead += numberOfShort

                    waveform?.updateAudioData(audioBuffer)
                }

                audio.stop()
                audio.release()
            }
        }
    }

}