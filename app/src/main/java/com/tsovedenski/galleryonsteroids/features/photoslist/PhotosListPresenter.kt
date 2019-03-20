package com.tsovedenski.galleryonsteroids.features.photoslist

import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.common.Try
import com.tsovedenski.galleryonsteroids.features.common.Presenter

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class PhotosListPresenter (
    private val view: PhotosListContract.View,
    private val model: PhotosListContract.ViewModel,
    contextProvider: CoroutineContextProvider
) : Presenter<PhotosListEvent>(contextProvider)
{
    override fun onChanged(e: PhotosListEvent) = when (e) {
        PhotosListEvent.OnStart -> onStart()
        PhotosListEvent.OnResume -> onResume()
        PhotosListEvent.OnDestroy -> onDestroy()
    }

    private fun onStart() {
    }

    private fun onResume() {
    }
}