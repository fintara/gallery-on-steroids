package com.tsovedenski.galleryonsteroids.features.creator.modes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.Mode
import com.otaliastudios.cameraview.PictureResult
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import kotlinx.android.synthetic.main.creator_mode_photovideo.*
import java.io.File

class CreatorPhotoFragment : Fragment(), CreatorMode {

    override var onRecordingFinished: ((Media) -> Unit)? = null

    private val media = Media(type = MediaType.Photo)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.creator_mode_photovideo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraview.mode = Mode.PICTURE
        cameraview.setLifecycleOwner(viewLifecycleOwner)
        cameraview.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                result.toFile(File(media.path)) {
                    onRecordingFinished?.let { it(media) }
                }
            }
        })
    }

    override fun startRecording() {
        cameraview.takePicture()
    }

    override fun stopRecording() = Unit
}