package com.tsovedenski.galleryonsteroids.features.photoeditor.tools.style

import android.content.Context
import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicConvolve3x3
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Created by Tsvetan Ovedenski on 24/04/19.
 */
sealed class Style (val name: String) {

    val transformation: Transformation<Bitmap> =
        StyleTransformation(this)
    abstract fun applyTo(pool: BitmapPool, bitmap: Bitmap, outWidth: Int, outHeight: Int): Bitmap

    object BW : Style("Black&White") {
        private val colorMatrix = ColorMatrix().apply {
            setSaturation(0f)
        }

        override fun applyTo(pool: BitmapPool, bitmap: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
            return bitmap.applyColorMatrix(pool,
                colorMatrix, outWidth, outHeight)
        }
    }

    object Sepia : Style("Sepia") {
        private val colorMatrix = ColorMatrix().apply {
            setSaturation(0f)
            val sepia = ColorMatrix().apply {
                setScale(1f, 0.95f, 0.82f, 1f)
            }
            postConcat(sepia)
        }

        override fun applyTo(pool: BitmapPool, bitmap: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
            return bitmap.applyColorMatrix(pool,
                colorMatrix, outWidth, outHeight)
        }
    }

    object Yellow : Style("Yellow") {
        private val colorMatrix = ColorMatrix(floatArrayOf(
            1.22994596833595f, 0.0209523774645382f, 0.383244054685119f, 0f, 0.450138899443543f,
            1.18737418804171f, -0.106933249401007f, 0f - 0.340084867779496f, 0.131673434493755f,
            1.06368919471589f, 0f, 0f, 0f, 0f, 11.91f,
            11.91f, 11.91f, 0f, 0f, 0f
        ))

        override fun applyTo(pool: BitmapPool, bitmap: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
            return bitmap.applyColorMatrix(pool,
                colorMatrix, outWidth, outHeight)
        }
    }

    object Pencil : Style("Pencil") {
        private val colorMatrix = ColorMatrix().apply {
            setSaturation(0f)

            val m = 255f
            val t = -255 * 128f
            val effect = ColorMatrix(floatArrayOf(
                m, 0f, 0f, 1f, t,
                0f, m, 0f, 1f, t,
                0f, 0f, m, 1f, t,
                0f, 0f, 0f, 1f, 0f
            ))

            postConcat(effect)
        }

        override fun applyTo(pool: BitmapPool, bitmap: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
            return bitmap.applyColorMatrix(pool,
                colorMatrix, outWidth, outHeight)
        }
    }

    object Invert : Style("Invert") {
        private val colorMatrix = ColorMatrix(
            floatArrayOf(
                -1f, 0f, 0f, 0f, 255f,
                0f, -1f, 0f, 0f, 255f,
                0f, 0f, -1f, 0f, 255f,
                0f, 0f, 0f, 1f, 0f
            ))

        override fun applyTo(pool: BitmapPool, bitmap: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
            return bitmap.applyColorMatrix(pool,
                colorMatrix, outWidth, outHeight)
        }
    }

    class Edges(private val context: Context) : Style("Edges") {
        private val coeffs = floatArrayOf(
            -1f, -1f, -1f,
            -1f,  8.25f, -1f,
            -1f, -1f, -1f
        )

        override fun applyTo(pool: BitmapPool, bitmap: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
            val result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888)

            val rs = RenderScript.create(context)

            val allocIn = Allocation.createFromBitmap(rs, bitmap)
            val allocOut = Allocation.createFromBitmap(rs, result)

            ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs)).apply {
                setInput(allocIn)
                setCoefficients(coeffs)
                forEach(allocOut)
            }

            allocOut.copyTo(result)
            rs.destroy()

            return result
        }
    }

    protected fun Bitmap.applyColorMatrix(pool: BitmapPool, colorMatrix: ColorMatrix, outWidth: Int, outHeight: Int): Bitmap {
        val result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)

        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }

        canvas.drawBitmap(this, 0f, 0f, paint)
        return result
    }

    companion object {
        fun createAll(context: Context) = listOf(
            BW,
            Sepia,
            Pencil,
            Invert,
            Yellow,
            Edges(context)
        )
    }
}

class StyleTransformation(val style: Style) : BitmapTransformation() {
    companion object {
        private var counter = 0
    }

    private var id = ++counter

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(style.name.toByteArray())
    }

    override fun equals(other: Any?): Boolean {
        return (other as? StyleTransformation)?.style?.name == style.name
    }

    override fun hashCode(): Int {
        return counter
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap = style.applyTo(pool, toTransform, outWidth, outHeight)
}