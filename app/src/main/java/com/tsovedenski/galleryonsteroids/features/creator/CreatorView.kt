package com.tsovedenski.galleryonsteroids.features.creator

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.leinardi.android.speeddial.SpeedDialView
import com.tsovedenski.galleryonsteroids.*
import com.tsovedenski.galleryonsteroids.common.toDurationString
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.common.*
import com.tsovedenski.galleryonsteroids.features.creator.modes.CreatorMode
import com.tsovedenski.galleryonsteroids.features.creator.modes.CreatorPhotoFragment
import com.tsovedenski.galleryonsteroids.features.creator.modes.CreatorVideoFragment
import com.tsovedenski.galleryonsteroids.features.creator.modes.CreatorVoiceFragment
import kotlinx.android.synthetic.main.activity_creator.*
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class CreatorView : Fragment(), CreatorContract.View {

    private val args by navArgs<CreatorViewArgs>()

    @Inject lateinit var injector: CreatorInjector

    private val event = MutableLiveData<CreatorEvent>()

    private lateinit var mode: CreatorMode

    private val handler by lazy { Handler() }

    private var time = 0L

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

    private val fabListener = object: SpeedDialView.OnChangeListener {
        override fun onMainActionSelected(): Boolean {
            Timber.i("Record pressed")
            event.value = CreatorEvent.RecordPressed
            return true
        }
        override fun onToggleChanged(isOpen: Boolean) = Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.appComponent.inject(this)
        injector.attachPresenter(this)
        Timber.tag(CreatorView::class.java.simpleName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_creator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, OnBackPressedCallback {
            findNavController().navigate(CreatorViewDirections.actionCreatorViewToMediaListView())
            true
        })

        photo_btn.setOnClickListener { event.value = CreatorEvent.ChangeType(MediaType.Photo) }
        video_btn.setOnClickListener { event.value = CreatorEvent.ChangeType(MediaType.Video) }
        voice_btn.setOnClickListener { event.value = CreatorEvent.ChangeType(MediaType.Audio) }

        creator_action.setOnChangeListener(fabListener)
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart")
        val type = MediaType.fromString(args.type) ?: MediaType.Photo
        event.value = CreatorEvent.OnStart(type)
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume")
        event.value = CreatorEvent.OnResume
        handler.postDelayed({ enterFullscreen() }, 350)
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop")
        exitFullscreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
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


        setFragment(mode as Fragment, "mode", R.id.imageview)
    }

    override fun startRecording() {
        mode.startRecording()

        recordAnimation.start()
        types_container.visibility = View.GONE
        requireActivity().requestedOrientation = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Configuration.ORIENTATION_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else -> requireActivity().requestedOrientation
        }
    }

    override fun stopRecording() {
        mode.stopRecording()
        handler.removeCallbacksAndMessages(null)

        spinner.visibility = View.VISIBLE
        imageview.visibility = View.GONE

        recordAnimation.cancel()
        creator_action.mainFabClosedBackgroundColor = resources.getColor(R.color.record, theme)
    }

    override fun startStopwatch() {
        time = 0
        stopwatchUpdate()
    }

    override fun openDetails(media: Media) {
        findNavController().navigate(CreatorViewDirections.actionCreatorViewToFormView(media))
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

    private fun stopwatchUpdate() {
        stopwatch.text = time.toDurationString()
        time += STOPWATCH_UPDATE_RATE
        handler.postDelayed(::stopwatchUpdate, STOPWATCH_UPDATE_RATE)
    }

    companion object {
        private const val RC_PERMISSIONS = 10
        private const val STOPWATCH_UPDATE_RATE = 500L
    }
}
