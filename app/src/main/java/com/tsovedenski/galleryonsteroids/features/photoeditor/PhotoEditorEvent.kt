package com.tsovedenski.galleryonsteroids.features.photoeditor

import android.graphics.Bitmap
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Created by Tsvetan Ovedenski on 20/04/2019.
 */
sealed class PhotoEditorEvent {
    data class OnStart(val media: Media) : PhotoEditorEvent()
    object OnResume : PhotoEditorEvent()
    object OnDestroy : PhotoEditorEvent()

    data class ToolSelected(val value: Tool) : PhotoEditorEvent()
    data class PhotoModified(val value: PhotoModification?) : PhotoEditorEvent()
}

sealed class Tool {
    object Crop : Tool()
    object Tune : Tool()
    object Style : Tool()
}

sealed class PhotoModification (val bitmap: Bitmap) {
    fun toFile(file: File) {
        BufferedOutputStream(FileOutputStream(file)).use { os ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, os)
        }
    }

    class Cropped(bitmap: Bitmap) : PhotoModification(bitmap)

    class Styled(bitmap: Bitmap) : PhotoModification(bitmap)

}