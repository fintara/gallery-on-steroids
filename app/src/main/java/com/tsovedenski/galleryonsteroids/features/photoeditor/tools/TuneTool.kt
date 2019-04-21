package com.tsovedenski.galleryonsteroids.features.photoeditor.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.tsovedenski.galleryonsteroids.GlideApp
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.features.photoeditor.PhotoModification
import jp.wasabeef.glide.transformations.BitmapTransformation
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation
import kotlinx.android.synthetic.main.fragment_tool_tune.*
import timber.log.Timber
import java.security.MessageDigest


/**
 * Created by Tsvetan Ovedenski on 21/04/19.
 */
class TuneTool : ToolFragment() {

    private val args by navArgs<TuneToolArgs>()

    override val layoutId = R.layout.fragment_tool_tune

    private lateinit var _modification: PhotoModification.Tuned
    override val modification: PhotoModification
        get() = _modification

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bars.adapter = TuneToolAdapter(Bar.all)
        GlideApp.with(this)
            .load(args.mediaUri)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .into(preview)
    }

    inner class TuneToolAdapter (
        private val bars: List<Bar>
    ) : RecyclerView.Adapter<TuneToolAdapter.ViewHolder>() {

        override fun getItemCount(): Int = bars.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(inflater.inflate(R.layout.tool_tune_item, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val bar = bars[position]

            holder.apply {
                label.text = bar.toString()
                seekbar.min = bar.min
                seekbar.max = bar.max
                seekbar.setProgress(bar.current, false)
            }

            holder.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (!fromUser) {
                        return
                    }
                    bar.current = progress
                    holder.label.text = bar.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    Timber.i("Finished ${bar.label} as ${bar.current}")

                    holder.label.text = bar.toString()

                    val multi = MultiTransformation<Bitmap>(bars.map(Bar::applyEffect))

                    spinner.show()
                    GlideApp.with(this@TuneTool)
                        .asBitmap()
                        .load(args.mediaUri)
                        .transition(withCrossFade())
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .transform(multi)
                        .signature(ObjectKey(bars.joinToString("|", transform = Bar::toString)))
                        .into(object : CustomViewTarget<ImageView, Bitmap>(preview) {
                            override fun onLoadFailed(errorDrawable: Drawable?) = Unit
                            override fun onResourceCleared(placeholder: Drawable?) = Unit

                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                _modification = PhotoModification.Tuned(resource.copy(Bitmap.Config.RGB_565, false))
                                preview.setImageBitmap(resource)
                                spinner.hide()
                            }
                        })
                }
            })
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val label: TextView = view.findViewById(R.id.bar_label)
            val seekbar: SeekBar = view.findViewById(R.id.seekbar)
        }
    }
}

sealed class Bar (val label: String) {
    open var current: Int = 0

    abstract val min: Int
    abstract val max: Int

    abstract fun applyEffect(): Transformation<Bitmap>

    override fun toString(): String {
        return "$label ($current)"
    }

    object Brightness : Bar("Brightness") {
        private const val mult = 50

        override val min = -mult
        override val max = mult

        override fun applyEffect(): Transformation<Bitmap> {
            return BrightnessFilterTransformation(current / mult.toFloat())
        }
    }

    object Contrast : Bar("Contrast") {
        private const val mult = 10

        override var current = 10
        override val min = 0
        override val max = 4 * mult

        override fun applyEffect(): Transformation<Bitmap> {
            return ContrastFilterTransformation(current / mult.toFloat())
        }
    }

    object Toon : Bar("Toon") {
        private const val mult = 100

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

    object Blur : Bar("Blur") {
        override val min = 0
        override val max = 32

        override fun applyEffect(): Transformation<Bitmap> {
            if (current < 1) {
                return NoopTransformation
            }
            return BlurTransformation(current, 2)
        }
    }

    object Pixelation : Bar("Pixelation") {
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
        val all = listOf(Brightness, Contrast, Toon, Pixelation, Blur)
    }
}