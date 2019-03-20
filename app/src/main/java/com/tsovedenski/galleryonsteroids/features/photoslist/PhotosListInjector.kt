package com.tsovedenski.galleryonsteroids.features.photoslist

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProviderImpl

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class PhotosListInjector (
    private val application: Application
) {
//    fun attachPresenter(view: PhotosListView): PhotosListPresenter {
//        val presenter = PhotosListPresenter(
//            view,
//            ViewModelProviders.of(view).get(PhotosListViewModel::class.java),
//            CoroutineContextProviderImpl
//        )
//        view.setObserver(presenter)
//        return presenter
//    }
}