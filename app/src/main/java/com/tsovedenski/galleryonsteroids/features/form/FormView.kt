package com.tsovedenski.galleryonsteroids.features.form

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tsovedenski.galleryonsteroids.*
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import kotlinx.android.synthetic.main.activity_form.*
import timber.log.Timber
import javax.inject.Inject

class FormView : Fragment(), FormContract.View {

    @Inject lateinit var injector: FormInjector

    private val event = MutableLiveData<FormEvent>()

    private val args by navArgs<FormViewArgs>()

    private val onBackPressedListener = OnBackPressedCallback {
        event.value = FormEvent.BackPressed
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Timber.tag(FormView::class.java.simpleName)
        application.appComponent.inject(this)
        injector.attachPresenter(this)
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_form, container, false)
    }

    override fun onStart() {
        super.onStart()
        val media = args.media

        event.value = FormEvent.OnStart(media)

        requireActivity().title = "${resources.getString(R.string.save_new)} ${resources.getString(when (media.type) {
            MediaType.Photo -> R.string.photo
            MediaType.Video -> R.string.video
            MediaType.Audio -> R.string.voice
        })}"
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume")
        event.value = FormEvent.OnResume
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause")
        hideKeyboard()
        event.value = FormEvent.OnPause
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
        event.value = FormEvent.OnDestroy
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            event.value = FormEvent.Save(media_title.text.toString())
            return true
        }

        return false
    }

    override fun setThumbnail(media: Media) {
        GlideApp
            .with(this)
            .load(media.thumbnailPath)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(media_thumbnail)
    }

    override fun showMessage(resId: Int) {
        showToast(resId)
    }

    override fun confirmDiscard() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.discard_title)

            setPositiveButton(R.string.discard) { _, _ ->
                event.value = FormEvent.DiscardConfirmed
            }

            setNegativeButton(R.string.stay) { dialog, _ ->
                dialog.cancel()
            }
        }.show()
    }

    override fun setObserver(observer: Observer<FormEvent>) {
        event.observeForever(observer)
    }

    override fun openCreator() {
        findNavController().navigateUp()
    }

    override fun openMediaList() {
        findNavController().navigate(FormViewDirections.actionFormViewToMediaListView())
    }
}