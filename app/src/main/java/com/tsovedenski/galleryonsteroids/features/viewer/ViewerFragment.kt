package com.tsovedenski.galleryonsteroids.features.viewer

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 06/04/19.
 */
abstract class ViewerFragment : Fragment(), ViewerTypeContract.View {

    protected val event = MutableLiveData<ViewerTypeEvent>()

    override fun onStart() {
        super.onStart()
        val media = arguments?.getParcelable<Media>(ViewerActivity.INTENT_EXTRA_MEDIA)
            ?: return requireActivity().finish()
        event.value = ViewerTypeEvent.OnStart(media)
    }

    override fun onResume() {
        super.onResume()
        event.value = ViewerTypeEvent.OnResume
    }

    override fun onDestroy() {
        super.onDestroy()
        event.value = ViewerTypeEvent.OnDestroy
    }

    override fun setObserver(observer: Observer<ViewerTypeEvent>) {
        event.observeForever(observer)
    }
}