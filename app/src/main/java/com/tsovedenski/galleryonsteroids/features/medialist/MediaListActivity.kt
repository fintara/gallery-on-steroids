package com.tsovedenski.galleryonsteroids.features.medialist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tsovedenski.galleryonsteroids.MyApplication
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.setFragment
import javax.inject.Inject

class MediaListActivity : AppCompatActivity() {

    @Inject lateinit var injector: MediaListInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_list)

        (application as MyApplication).appComponent.inject(this)

        val fragmentTag = "MEDIA_LIST"
        val fragment = supportFragmentManager.findFragmentByTag(fragmentTag) as? MediaListView
            ?: MediaListView.newInstance()

        setFragment(fragment, fragmentTag, R.id.container)

        injector.attachPresenter(fragment)
    }
}
