package com.tsovedenski.galleryonsteroids.features.photoeditor

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.target.Target
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tsovedenski.galleryonsteroids.GlideApp
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.features.common.NavigationResult
import com.tsovedenski.galleryonsteroids.features.common.application
import kotlinx.android.synthetic.main.fragment_photoeditor.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Tsvetan Ovedenski on 20/04/2019.
 */
class PhotoEditorView : Fragment(), PhotoEditorContract.View, NavigationResult<PhotoModification> {

    @Inject lateinit var injector: PhotoEditorInjector

    private val event = MutableLiveData<PhotoEditorEvent>()

    private val args by navArgs<PhotoEditorViewArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.appComponent.inject(this)
        injector.attachPresenter(this)
        Timber.tag(PhotoEditorView::class.java.simpleName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photoeditor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolMenu.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.photoeditor_crop -> event.value = PhotoEditorEvent.ToolSelected(Tool.Crop)
                R.id.photoeditor_tune -> event.value = PhotoEditorEvent.ToolSelected(Tool.Tune)
                R.id.photoeditor_style -> event.value = PhotoEditorEvent.ToolSelected(Tool.Style)
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        event.value = PhotoEditorEvent.OnStart(args.media)
    }

    override fun onResume() {
        super.onResume()
        event.value = PhotoEditorEvent.OnResume
    }

    override fun onDestroy() {
        super.onDestroy()
        event.value = PhotoEditorEvent.OnDestroy
    }

    override fun setImage(media: Media) {
        GlideApp.with(this)
            .load(media.path)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(preview)
    }

    override fun setImage(bitmap: Bitmap) {
        preview.setImageBitmap(bitmap)
    }

    override fun openCrop(mediaUri: String) {
        findNavController().navigate(PhotoEditorViewDirections.actionPhotoEditorViewToCropTool(mediaUri))
    }

    override fun openTune(mediaUri: String) {
        TODO("not implemented")
    }

    override fun openStyle(mediaUri: String) {
        TODO("not implemented")
    }

    override fun onNavigationResult(payload: PhotoModification) {
        Timber.i("Got payload: ${payload.javaClass.simpleName}")
        event.value = PhotoEditorEvent.PhotoModified(payload)
    }

    override fun showLoader() {
        spinner.visibility = View.VISIBLE
        preview.visibility = View.INVISIBLE
    }

    override fun hideLoader() {
        spinner.visibility = View.GONE
        preview.visibility = View.VISIBLE
    }

    override fun setObserver(observer: Observer<PhotoEditorEvent>) {
        event.observeForever(observer)
    }
}