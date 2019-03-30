package com.tsovedenski.galleryonsteroids.features.creator

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.leinardi.android.speeddial.SpeedDialView
import com.tsovedenski.galleryonsteroids.MyApplication
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.setFragment
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

        val animation = ObjectAnimator.ofArgb(
            creator_action,
            "mainFabClosedBackgroundColor",
            resources.getColor(R.color.record, theme),
            resources.getColor(R.color.recordHighlight, theme)
        ).apply {
            duration = 450
            repeatCount = Animation.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }

        // todo: remove this
        var animated = false
        creator_action.setOnChangeListener(object: SpeedDialView.OnChangeListener {
            override fun onMainActionSelected(): Boolean {
                if (animated) {
                    println("Clear animation")
                    animation.cancel()
                    creator_action.mainFabClosedBackgroundColor = resources.getColor(R.color.record, theme)
                    types_container.visibility = View.VISIBLE
                } else {
                    println("Start animation")
                    animation.start()
                    types_container.visibility = View.GONE
                }
                animated = !animated
                return true
            }

            override fun onToggleChanged(isOpen: Boolean) {
                TODO("not implemented")
            }

        })
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
        updateButtonHighlight(value)

        if (value == MediaType.Audio) {
            val fragment = CreatorVoiceFragment()
            setFragment(fragment, "mode", R.id.container)
        }
    }

    private fun updateButtonHighlight(value: MediaType) {
        val transparent = resources.getColor(android.R.color.transparent, theme)
        val selected = resources.getColor(R.color.white_25t, theme)

        listOf(photo_btn, video_btn, voice_btn).forEach { it.setBackgroundColor(transparent) }

        val button = when (value) {
            MediaType.Photo -> photo_btn
            MediaType.Video -> video_btn
            MediaType.Audio -> voice_btn
        }

        button.setBackgroundColor(selected)
    }
}
