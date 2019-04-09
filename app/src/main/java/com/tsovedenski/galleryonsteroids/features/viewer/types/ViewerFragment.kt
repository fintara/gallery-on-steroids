package com.tsovedenski.galleryonsteroids.features.viewer.types

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerView
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerTypeContract
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerTypeEvent
import timber.log.Timber

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
abstract class ViewerFragment : Fragment(), ViewerTypeContract.View {

    init {
        Timber.tag(ViewerFragment::class.java.name)
    }

    protected val event = MutableLiveData<ViewerTypeEvent>()

    override fun onStart() {
        super.onStart()
        val media = arguments?.getParcelable<Media>(ViewerView.INTENT_EXTRA_MEDIA)
            ?: return requireActivity().finish()
        event.value = ViewerTypeEvent.OnStart(media)
        Timber.i("onStart(mediaId=${media.id})")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume")
        event.value = ViewerTypeEvent.OnResume
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
        event.value = ViewerTypeEvent.OnDestroy
    }

    override fun setObserver(observer: Observer<ViewerTypeEvent>) {
        event.observeForever(observer)
    }
}