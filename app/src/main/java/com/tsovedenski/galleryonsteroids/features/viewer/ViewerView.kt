package com.tsovedenski.galleryonsteroids.features.viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tsovedenski.galleryonsteroids.*
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.common.application
import com.tsovedenski.galleryonsteroids.features.common.enterFullscreen
import com.tsovedenski.galleryonsteroids.features.common.exitFullscreen
import com.tsovedenski.galleryonsteroids.features.common.setFragment
import com.tsovedenski.galleryonsteroids.features.viewer.types.AudioViewerFragment
import com.tsovedenski.galleryonsteroids.features.viewer.types.PhotoViewerFragment
import com.tsovedenski.galleryonsteroids.features.viewer.types.VideoViewerFragment
import com.tsovedenski.galleryonsteroids.features.viewer.types.ViewerFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
class ViewerView : Fragment() {

    @Inject lateinit var injector: ViewerTypeInjector

    private val args by navArgs<ViewerViewArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.tag(ViewerView::class.java.name)
        application.appComponent.inject(this)
        requireActivity().onBackPressedDispatcher.addCallback(OnBackPressedCallback {
            findNavController().navigateUp()
        })

        val media = args.media
        initFragment(media)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.feature_viewer, container, false)
    }

    override fun onResume() {
        super.onResume()
        enterFullscreen()
    }

    override fun onStop() {
        super.onStop()
        exitFullscreen()
    }

    private fun initFragment(media: Media) {
        val fragment = childFragmentManager.findFragmentByTag(CONTAINER_TAG) as? ViewerFragment
            ?: media.resolveFragment()

        Timber.i("Resolved fragment ${fragment.javaClass.name}")

        setFragment(fragment, CONTAINER_TAG, R.id.imageview)
        injector.attachPresenter(fragment)
    }

    private fun Media.resolveFragment(): ViewerFragment = when (type) {
        MediaType.Photo -> PhotoViewerFragment.newInstance(this)
        MediaType.Video -> VideoViewerFragment.newInstance(this)
        MediaType.Audio -> AudioViewerFragment.newInstance(this)
    }

    companion object {
        const val INTENT_EXTRA_MEDIA = "media"

        private const val CONTAINER_TAG = "main_content"
    }
}