package com.tsovedenski.galleryonsteroids.features.photoeditor.tools

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.features.common.setTitle
import com.tsovedenski.galleryonsteroids.features.photoeditor.PhotoModification
import kotlinx.android.synthetic.main.photoeditor_tool_crop.*

/**
 * Created by Tsvetan Ovedenski on 21/04/19.
 */
class CropTool : ToolFragment() {

    override val layoutId = R.layout.photoeditor_tool_crop

    private val args by navArgs<CropToolArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cropper.setImageUriAsync(Uri.parse(args.mediaUri))
        setTitle(R.string.crop)
    }

    override val modification: PhotoModification
        get() = PhotoModification.Cropped(cropper.croppedImage)
}