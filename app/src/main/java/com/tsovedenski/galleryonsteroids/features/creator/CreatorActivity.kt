package com.tsovedenski.galleryonsteroids.features.creator

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.leinardi.android.speeddial.SpeedDialView
import com.tsovedenski.galleryonsteroids.MyApplication
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.common.hasPermissions
import com.tsovedenski.galleryonsteroids.features.common.requestPermissions
import com.tsovedenski.galleryonsteroids.features.creator.modes.CreatorMode
import com.tsovedenski.galleryonsteroids.features.creator.modes.CreatorPhotoFragment
import com.tsovedenski.galleryonsteroids.features.creator.modes.CreatorVideoFragment
import com.tsovedenski.galleryonsteroids.features.creator.modes.CreatorVoiceFragment
import com.tsovedenski.galleryonsteroids.features.form.FormActivity
import com.tsovedenski.galleryonsteroids.setFragment
import kotlinx.android.synthetic.main.activity_creator.*
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class CreatorActivity : AppCompatActivity(), CreatorContract.View {

    @Inject lateinit var injector: CreatorInjector

    private val event = MutableLiveData<CreatorEvent>()

    private lateinit var mode: CreatorMode

    private val recordAnimation by lazy {
        ObjectAnimator.ofArgb(
            creator_action,
            "mainFabClosedBackgroundColor",
            resources.getColor(R.color.record, theme),
            resources.getColor(R.color.recordHighlight, theme)
        ).apply {
            duration = 450
            repeatCount = Animation.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }
    }

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

        creator_action.setOnChangeListener(object: SpeedDialView.OnChangeListener {
            override fun onMainActionSelected(): Boolean {
                event.value = CreatorEvent.RecordPressed
                return true
            }
            override fun onToggleChanged(isOpen: Boolean) = Unit
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun setObserver(observer: Observer<CreatorEvent>) {
        event.observeForever(observer)
    }

    override fun setMediaType(value: MediaType) {
        updateButtonHighlight(value)
        updateRecordButtonIcon(value)

        mode = when (value) {
            MediaType.Photo -> CreatorPhotoFragment()
            MediaType.Video -> CreatorVideoFragment()
            MediaType.Audio -> CreatorVoiceFragment()
        }

        mode.onRecordingFinished = { media ->
            event.value = CreatorEvent.RecordedMedia(media)
        }

        setFragment(mode as Fragment, "mode", R.id.container)
    }

    override fun startRecording() {
        mode.startRecording()

        recordAnimation.start()
        types_container.visibility = View.GONE
    }

    override fun stopRecording() {
        mode.stopRecording()

        spinner.visibility = View.VISIBLE
        container.visibility = View.GONE

        recordAnimation.cancel()
        creator_action.mainFabClosedBackgroundColor = resources.getColor(R.color.record, theme)
    }

    override fun openDetails(media: Media) {
        val intent = Intent(this, FormActivity::class.java).apply {
            putExtra("media", media)
        }
        startActivity(intent)
        finish()
    }

    override fun checkPermissions(@StringRes rationaleResId: Int, vararg perms: String) {
        if (hasPermissions(*perms)) {
            return
        }

        requestPermissions(rationaleResId, RC_PERMISSIONS, *perms)
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

    private fun updateRecordButtonIcon(value: MediaType) {
        val icon = when (value) {
            MediaType.Photo -> R.drawable.camera
            MediaType.Video -> R.drawable.video
            MediaType.Audio -> R.drawable.microphone
        }

        creator_action.setMainFabClosedDrawable(resources.getDrawable(icon, theme))
    }

    companion object {
        private const val RC_PERMISSIONS = 10
    }
}
