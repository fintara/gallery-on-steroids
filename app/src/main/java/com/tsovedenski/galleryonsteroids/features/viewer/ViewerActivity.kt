package com.tsovedenski.galleryonsteroids.features.viewer

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tsovedenski.galleryonsteroids.MyApplication
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.viewer.types.VideoViewerFragment
import com.tsovedenski.galleryonsteroids.setFragment
import com.tsovedenski.galleryonsteroids.showToast
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
class ViewerActivity : AppCompatActivity() {

    @Inject lateinit var injector: ViewerTypeInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)

        Timber.tag(ViewerActivity::class.java.name)
        (application as MyApplication).appComponent.inject(this)

        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        if (savedInstanceState == null) {
            val media = intent.getParcelableExtra<Media>(INTENT_EXTRA_MEDIA) ?: return intentMediaKeyMissing()
            initFragment(media)
        }
    }

    private fun initFragment(media: Media) {
        val fragment = supportFragmentManager.findFragmentByTag(CONTAINER_TAG) as? ViewerFragment
            ?: media.resolveFragment()

        setFragment(fragment, CONTAINER_TAG, R.id.container)
        injector.attachPresenter(fragment)
    }

    private fun Media.resolveFragment(): ViewerFragment = when (type) {
        MediaType.Photo -> VideoViewerFragment.newInstance(this)
        MediaType.Video -> VideoViewerFragment.newInstance(this)
        MediaType.Audio -> VideoViewerFragment.newInstance(this)
    }

    private fun intentMediaKeyMissing() {
        Timber.e("Intent extra '$INTENT_EXTRA_MEDIA' was null")
        showToast("Media is required")
        finish()
    }

    companion object {
        const val INTENT_EXTRA_MEDIA = "media"

        private const val CONTAINER_TAG = "main_content"
    }
}