package com.tsovedenski.galleryonsteroids.features.photoslist

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class PhotosListView : Fragment(), PhotosListContract.View {

    private val event = MutableLiveData<PhotosListEvent>()

    override fun setObserver(observer: Observer<PhotosListEvent>) {
        event.observeForever(observer)
    }

    companion object {
        fun newInstance() = PhotosListView()
    }
}