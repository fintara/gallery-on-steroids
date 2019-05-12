package com.tsovedenski.galleryonsteroids.features.photoeditor

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
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
import com.tsovedenski.galleryonsteroids.features.common.*
import kotlinx.android.synthetic.main.feature_photoeditor.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Tsvetan Ovedenski on 20/04/2019.
 */
class PhotoEditorView : Fragment(), PhotoEditorContract.View, NavigationResult<PhotoModification?> {

    @Inject lateinit var injector: PhotoEditorInjector

    private val event = MutableLiveData<PhotoEditorEvent>()

    private val args by navArgs<PhotoEditorViewArgs>()

    private val onBackPressedListener = OnBackPressedCallback {
        event.value = PhotoEditorEvent.BackPressed
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        application.appComponent.inject(this)
        injector.attachPresenter(this)
        Timber.tag(PhotoEditorView::class.java.simpleName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.feature_photoeditor, container, false)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tool, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.confirm) {
            event.value = PhotoEditorEvent.Confirmed
            return true
        }

        return false
    }

    override fun onStart() {
        super.onStart()
        setTitle(R.string.edit_photo)
        event.value = PhotoEditorEvent.OnStart(args.media)
    }

    override fun onResume() {
        super.onResume()
        event.value = PhotoEditorEvent.OnResume
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        resetTitle()
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
        findNavController().navigate(PhotoEditorViewDirections.actionPhotoEditorViewToTuneTool(mediaUri))
    }

    override fun openStyle(mediaUri: String) {
        findNavController().navigate(PhotoEditorViewDirections.actionPhotoEditorViewToStyleTool(mediaUri))
    }

    override fun openDetails(media: Media) {
        findNavController().navigate(PhotoEditorViewDirections.actionPhotoEditorViewToFormView(media))
    }

    override fun openCreator() {
        findNavController().navigateUp()
    }

    override fun onNavigationResult(payload: PhotoModification?) {
        Timber.i("Got payload: ${payload?.javaClass?.simpleName}")
        event.value = PhotoEditorEvent.PhotoModified(payload)
    }

    override fun confirmDiscard() {
        discardDialog { event.value = PhotoEditorEvent.Discarded }
    }

    override fun showLoader() {
        spinner.show()
        preview.visibility = View.INVISIBLE
    }

    override fun hideLoader() {
        spinner.hide()
        preview.visibility = View.VISIBLE
    }

    override fun setObserver(observer: Observer<PhotoEditorEvent>) {
        event.observeForever(observer)
    }
}