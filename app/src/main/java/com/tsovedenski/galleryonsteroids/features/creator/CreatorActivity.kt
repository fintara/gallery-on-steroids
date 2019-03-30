package com.tsovedenski.galleryonsteroids.features.creator

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.MyApplication
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import kotlinx.android.synthetic.main.activity_creator.*
import javax.inject.Inject

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class CreatorActivity : AppCompatActivity(), CreatorContract.View {

    @Inject lateinit var injector: CreatorInjector
    private val event = MutableLiveData<CreatorEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)

        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        (application as MyApplication).appComponent.inject(this)

        injector.attachPresenter(this)

        photo_btn.setOnClickListener { event.value = CreatorEvent.ChangeType(MediaType.Photo) }
        video_btn.setOnClickListener { event.value = CreatorEvent.ChangeType(MediaType.Video) }
        voice_btn.setOnClickListener { event.value = CreatorEvent.ChangeType(MediaType.Audio) }
    }

    override fun onStart() {
        super.onStart()

        val type = intent.getStringExtra("type")
        event.value = CreatorEvent.OnStart(MediaType.fromString(type) ?: MediaType.Photo)
    }

    override fun onResume() {
        super.onResume()
        event.value = CreatorEvent.OnResume
    }

    override fun onDestroy() {
        super.onDestroy()
        event.value = CreatorEvent.OnDestroy
    }

    override fun setObserver(observer: Observer<CreatorEvent>) {
        event.observeForever(observer)
    }

    override fun setMediaType(value: MediaType) {
        val transparent = resources.getColor(android.R.color.transparent, null)
        val selected = resources.getColor(R.color.white_25t, null)

        listOf(photo_btn, video_btn, voice_btn).forEach { it.setBackgroundColor(transparent) }

        when (value) {
            MediaType.Photo -> photo_btn.setBackgroundColor(selected)
            MediaType.Video -> video_btn.setBackgroundColor(selected)
            MediaType.Audio -> voice_btn.setBackgroundColor(selected)
        }
    }
}
