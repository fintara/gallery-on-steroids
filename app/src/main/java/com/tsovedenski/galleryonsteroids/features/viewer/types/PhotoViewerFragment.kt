package com.tsovedenski.galleryonsteroids.features.viewer.types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tsovedenski.galleryonsteroids.GlideApp
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.common.prettyFormat
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.features.common.theme
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerTypeEvent
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerView
import com.tsovedenski.galleryonsteroids.services.ImageLabel
import kotlinx.android.synthetic.main.viewer_photo.*
import timber.log.Timber

/**
 * Created by Tsvetan Ovedenski on 07/04/19.
 */
class PhotoViewerFragment : ViewerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.viewer_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageview.setOnClickListener {
            event.value = ViewerTypeEvent.MediaClicked
        }
    }

    override fun prepare(media: Media) {
        Timber.i("Preparing ${media.id}")

        GlideApp
            .with(requireActivity())
            .load(media.path)
            .into(imageview)

        media_title.text = media.title
        media_date.text = media.createdAt.prettyFormat()
    }

    override fun showControls() {
        details_container?.animate()?.alpha(1f)?.start()
    }

    override fun hideControls() {
        details_container?.animate()?.alpha(0f)?.start()
    }

    override fun showLabelSpinner() {
        spinner?.show()
    }

    override fun hideLabelSpinner() {
        spinner?.hide()
    }

    override fun setLabels(list: List<ImageLabel>) {
        Timber.i("About to set labels: ${list.joinToString(", ", transform = { it.label })}")
        media_labels_container?.removeAllViews()
        list.forEach {
            TextView(requireContext())
                .apply {
                    text = "${it.label} (${"%.1f".format(it.confidence * 100)}%)  "
                    setTextColor(resources.getColor(R.color.white, theme))
                }
                .let { media_labels_container?.addView(it) }
        }
    }

    override fun setEmptyLabels(message: String) {
        media_labels_container?.removeAllViews()
        context?.let {
            TextView(it)
                .apply {
                    text = message
                    setTextColor(resources.getColor(R.color.white, theme))
                }
                .let { textView ->  media_labels_container?.addView(textView) }
        }
    }

    override fun setEmptyLabels(messageId: Int) {
        setEmptyLabels(resources.getString(messageId))
    }

    companion object {
        fun newInstance(media: Media) = PhotoViewerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ViewerView.INTENT_EXTRA_MEDIA, media)
            }
        }
    }
}