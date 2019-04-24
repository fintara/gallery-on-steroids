package com.tsovedenski.galleryonsteroids.features.photoeditor.tools.style

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.tsovedenski.galleryonsteroids.GlideApp
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.features.common.setTitle
import com.tsovedenski.galleryonsteroids.features.photoeditor.PhotoModification
import com.tsovedenski.galleryonsteroids.features.photoeditor.tools.StyleToolArgs
import com.tsovedenski.galleryonsteroids.features.photoeditor.tools.ToolFragment
import kotlinx.android.synthetic.main.fragment_tool_style.*

/**
 * Created by Tsvetan Ovedenski on 21/04/19.
 */
class StyleTool : ToolFragment() {

    override val layoutId = R.layout.fragment_tool_style

    private lateinit var _modification: PhotoModification.Styled
    override val modification: PhotoModification
        get() = _modification

    private val args by navArgs<StyleToolArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTitle(R.string.style)

        styles.adapter = StyleToolAdapter(
            Style.createAll(requireContext()),
            args.mediaUri,
            requireContext(),
            onSelected = ::drawWithStyle
        )

        drawOriginal()
    }

    private fun drawOriginal() {
        GlideApp.with(this)
            .load(args.mediaUri)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .signature(ObjectKey(Any()))
            .into(preview)
    }

    private fun drawWithStyle(style: Style) {
        spinner.show()
        GlideApp.with(this)
            .asBitmap()
            .load(args.mediaUri)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .signature(ObjectKey(style.name))
            .transform(style.transformation)
            .into(object : CustomViewTarget<ImageView, Bitmap>(preview) {
                override fun onLoadFailed(errorDrawable: Drawable?) = Unit
                override fun onResourceCleared(placeholder: Drawable?) = Unit

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    _modification = PhotoModification.Styled(resource.copy(Bitmap.Config.RGB_565, false))
                    preview.setImageBitmap(resource)
                    spinner.hide()
                }
            })
    }
}