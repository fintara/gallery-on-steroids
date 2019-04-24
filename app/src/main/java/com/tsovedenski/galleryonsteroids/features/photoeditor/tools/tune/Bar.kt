package com.tsovedenski.galleryonsteroids.features.photoeditor.tools.tune

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import jp.wasabeef.glide.transformations.BitmapTransformation
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation
import java.security.MessageDigest

/**
 * Created by Tsvetan Ovedenski on 24/04/19.
 */
sealed class Bar (val label: String) {
    open var current: Int = 0

    abstract val min: Int
    abstract val max: Int

    abstract fun applyEffect(): Transformation<Bitmap>

    override fun toString(): String {
        return "$label ($current)"
    }

    class Brightness : Bar("Brightness") {
        companion object {
            private const val mult = 50
        }

        override val min = -mult
        override val max =
            mult

        override fun applyEffect(): Transformation<Bitmap> {
            return BrightnessFilterTransformation(current / mult.toFloat())
        }
    }

    class Contrast : Bar("Contrast") {
        companion object {
            private const val mult = 10
        }

        override var current = 10
        override val min = 0
        override val max = 4 * mult

        override fun applyEffect(): Transformation<Bitmap> {
            return ContrastFilterTransformation(current / mult.toFloat())
        }
    }

    class Toon : Bar("Toon") {
        companion object {
            private const val mult = 100
        }

        override var current = 0
        override val min = 0
        override val max = mult

        override fun applyEffect(): Transformation<Bitmap> {
            if (current < 1) {
                return NoopTransformation
            }
            return ToonFilterTransformation(1f - current / mult.toFloat(), 12f)
        }
    }

    class Blur : Bar("Blur") {
        override val min = 0
        override val max = 32

        override fun applyEffect(): Transformation<Bitmap> {
            if (current < 1) {
                return NoopTransformation
            }
            return BlurTransformation(current, 2)
        }
    }

    class Pixelation : Bar("Pixelation") {
        override var current = 0

        override val min = 0
        override val max = 50

        override fun applyEffect(): Transformation<Bitmap> {
            if (current < 1) {
                return NoopTransformation
            }
            return PixelationFilterTransformation(current.toFloat())
        }
    }

    private object NoopTransformation : BitmapTransformation() {
        override fun hashCode(): Int {
            return 0
        }

        override fun equals(other: Any?): Boolean {
            return other is NoopTransformation
        }

        override fun updateDiskCacheKey(messageDigest: MessageDigest) {
            messageDigest.update("noop".toByteArray())
        }

        override fun transform(
            context: Context,
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
        ): Bitmap {
            return toTransform
        }
    }

    companion object {
        fun create() = listOf(
            Brightness(),
            Contrast(),
            Toon(),
            Pixelation(),
            Blur()
        )
    }
}