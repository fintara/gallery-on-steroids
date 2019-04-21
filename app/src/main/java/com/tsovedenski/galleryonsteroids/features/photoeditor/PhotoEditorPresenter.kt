package com.tsovedenski.galleryonsteroids.features.photoeditor

import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.features.common.Presenter
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File

/**
 * Created by Tsvetan Ovedenski on 21/04/19.
 */
class PhotoEditorPresenter (
    private val view: PhotoEditorContract.View,
    private val model: PhotoEditorContract.ViewModel,
    coroutineContextProvider: CoroutineContextProvider
) : Presenter<PhotoEditorEvent>(coroutineContextProvider) {

    init {
        Timber.tag(PhotoEditorPresenter::class.java.simpleName)
    }

    override fun onChanged(e: PhotoEditorEvent) = when (e) {
        is PhotoEditorEvent.OnStart -> onStart(e.media)
        PhotoEditorEvent.OnResume -> onResume()
        PhotoEditorEvent.OnDestroy -> onDestroy()
        is PhotoEditorEvent.ToolSelected -> toolSelected(e.value)
        is PhotoEditorEvent.PhotoModified -> photoModified(e.value)
    }

    private fun onStart(media: Media) {
        model.media = media
        Timber.i("onStart")
    }

    private fun onResume() {
        Timber.i("onResume")
        if (!model.loaded) {
            model.media?.let(view::setImage)
            model.loaded = true
        }
    }

    private fun toolSelected(tool: Tool) {
        val media = model.media ?: return

        val tempFile = media.tempFile()
        File(media.path).copyTo(tempFile, overwrite = true)
        model.tempFile = tempFile

        val tempFileUri = tempFile.toURI().toASCIIString()

        val open = when (tool) {
            Tool.Crop -> view::openCrop
            Tool.Tune -> view::openTune
            Tool.Style -> view::openStyle
        }

        open(tempFileUri)
    }

    private fun photoModified(modification: PhotoModification?) {
        val tempFile = model.tempFile ?: return

        when (modification) {
            null -> {
                toolDiscarded(tempFile)
            }

            else -> {
                view.setImage(modification.bitmap)

                Timber.i("About to save temp file")
                modification.toFile(tempFile)
                Timber.i("Saved temp file")

                toolApplied(tempFile)
                Timber.i("Applied tool")
            }
        }
    }

    private fun toolApplied(tempFile: File) {
        val media = model.media ?: return

        // copy temp to original
        tempFile.copyTo(File(media.path), overwrite = true)

        // delete temp
        tempFile.delete()
        model.tempFile = null
    }

    private fun toolDiscarded(tempFile: File) {
        // delete temp
        tempFile.delete()
        model.tempFile = null

        view.setImage(model.media ?: return)
    }

    private fun Media.tempFile() = File.createTempFile(id, "preview")
}