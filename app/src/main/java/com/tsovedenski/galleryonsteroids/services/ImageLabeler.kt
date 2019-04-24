package com.tsovedenski.galleryonsteroids.services

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlin.coroutines.suspendCoroutine

/**
 * Created by Tsvetan Ovedenski on 2019-04-24.
 */
interface ImageLabeler {
    suspend fun label(uriString: String): List<ImageLabel>
}

data class ImageLabel (val label: String, val confidence: Float)

class ImageLabelerImpl (
    private val context: Context
) : ImageLabeler {

    override suspend fun label(uriString: String): List<ImageLabel> = suspendCoroutine { c ->
        val image = FirebaseVisionImage.fromFilePath(context, Uri.parse(uriString))
        val detector = FirebaseVision.getInstance().onDeviceImageLabeler

        detector.processImage(image)
            .addOnSuccessListener { labels ->
                val string = labels
                    .sortedBy { -it.confidence }
                    .map { ImageLabel(it.text, it.confidence) }

                c.resumeWith(Result.success(string))
            }
            .addOnFailureListener { error ->
                c.resumeWith(Result.failure(error))
            }
    }
}