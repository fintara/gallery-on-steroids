package com.tsovedenski.galleryonsteroids.features.photoeditor.tools.tune

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.tsovedenski.galleryonsteroids.GlideApp
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.features.photoeditor.PhotoModification
import com.tsovedenski.galleryonsteroids.features.photoeditor.tools.ToolFragment
import kotlinx.android.synthetic.main.photoeditor_tool_tune.*


/**
 * Created by Tsvetan Ovedenski on 21/04/19.
 */
class TuneTool : ToolFragment() {

    private val args by navArgs<TuneToolArgs>()

    override val layoutId = R.layout.photoeditor_tool_tune

    private lateinit var _modification: PhotoModification.Tuned
    override val modification: PhotoModification
        get() = _modification

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bars.adapter = TuneToolAdapter(
            Bar.create(),
            onSliderChange = ::drawWithBars
        )

        drawOriginal()
    }

    private fun drawOriginal() {
        GlideApp.with(this)
            .load(args.mediaUri)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .into(preview)
    }

    private fun drawWithBars(bars: List<Bar>) {
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
}