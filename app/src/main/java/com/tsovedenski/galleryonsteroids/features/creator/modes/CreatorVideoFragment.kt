package com.tsovedenski.galleryonsteroids.features.creator.modes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.Mode
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import kotlinx.android.synthetic.main.fragment_mode_photovideo.*
import java.io.File

class CreatorVideoFragment : Fragment(), CreatorMode {

    override var onRecordingFinished: ((Media) -> Unit)? = null

    private val media = Media(type = MediaType.Video)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_mode_photovideo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraview.mode = Mode.VIDEO
        cameraview.setLifecycleOwner(viewLifecycleOwner)
        cameraview.addCameraListener(object : CameraListener() {
            override fun onVideoTaken(result: VideoResult) {
                onRecordingFinished?.let { it(media) }
            }
        })
    }

    override fun startRecording() {
        val file = File(media.path)

        if (!file.exists()) {
            file.createNewFile()
        }

        cameraview.takeVideo(file)
    }

    override fun stopRecording() {
        cameraview.stopVideo()
    }
}