package com.tsovedenski.galleryonsteroids.features.medialist

import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.common.Presenter
import com.tsovedenski.galleryonsteroids.services.MediaService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class MediaListPresenter (
    private val view: MediaListContract.View,
    private val model: MediaListContract.ViewModel,
    private val service: MediaService,
    private val adapter: MediaListAdapter,
    private val contextProvider: CoroutineContextProvider
) : Presenter<MediaListEvent>(contextProvider)
{
    override fun onChanged(e: MediaListEvent) = when (e) {
        MediaListEvent.OnStart -> onStart()
        MediaListEvent.OnResume -> onResume()
        MediaListEvent.OnDestroy -> onDestroy()

        MediaListEvent.CreatePhoto -> createPhoto()
        MediaListEvent.CreateVideo -> createVideo()
        MediaListEvent.CreateAudio -> createAudio()

        is MediaListEvent.ChangeViewType -> changeViewType(e.value)
    }

    private fun onStart() {
        adapter.setObserver(this)
        view.setAdapter(adapter)
    }

    private fun onResume() {
        loadItems()
        changeViewType(model.getViewType())
    }

    private fun loadItems() = launch {
        val items = service.getMedia()
        adapter.submitList(items)
    }

    private fun createPhoto() {
        view.openCreator(MediaType.Photo)
    }

    private fun createVideo() {
        view.openCreator(MediaType.Video)
    }

    private fun createAudio() {
        view.openCreator(MediaType.Audio)
    }

    private fun changeViewType(value: ViewType) {
        view.setViewType(value)
        model.setViewType(value)
        adapter.viewType = value
    }
}