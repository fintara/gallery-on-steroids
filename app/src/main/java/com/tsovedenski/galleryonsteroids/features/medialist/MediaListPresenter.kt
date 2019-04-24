package com.tsovedenski.galleryonsteroids.features.medialist

import android.Manifest
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.common.Presenter
import com.tsovedenski.galleryonsteroids.services.MediaService
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class MediaListPresenter (
    private val view: MediaListContract.View,
    private val model: MediaListContract.ViewModel,
    private val service: MediaService,
    private val adapter: MediaListAdapter,
    contextProvider: CoroutineContextProvider
) : Presenter<MediaListEvent>(contextProvider)
{
    init {
        Timber.tag(MediaListPresenter::class.java.simpleName)
    }

    override fun onChanged(e: MediaListEvent) {
        Timber.i("Received event: $e")
        when (e) {
            MediaListEvent.OnStart -> onStart()
            MediaListEvent.OnResume -> onResume()
            MediaListEvent.OnOptionsReady -> onOptionsReady()
            MediaListEvent.OnDestroy -> onDestroy()

            MediaListEvent.CreatePhoto -> createPhoto()
            MediaListEvent.CreateVideo -> createVideo()
            MediaListEvent.CreateAudio -> createAudio()

            is MediaListEvent.ChangeViewType -> changeViewType(e.value)
            is MediaListEvent.ItemSelected -> itemSelected(e.value)
            is MediaListEvent.OptionsSelected -> optionsSelected(e.value)
            is MediaListEvent.DeleteItem -> deleteItem(e.value)
        }
    }

    private fun onStart() {
        adapter.setObserver(this)
        view.setAdapter(adapter)
        view.checkPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun onResume() {
        loadItems()
    }

    private fun onOptionsReady() {
        changeViewType(model.viewType)
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
        model.viewType = value
        adapter.viewType = value
        Timber.i("Set view type: ${model.viewType}")
    }

    private fun itemSelected(value: Media) {
        view.openViewer(value)
    }

    private fun optionsSelected(value: Media) {
        view.showOptions(value)
    }

    private fun deleteItem(value: Media) {
        launch {
            val nextList = adapter.currentList.filter { it.id != value.id }
            adapter.submitList(nextList)
            service.delete(value)
        }
    }
}