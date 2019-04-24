package com.tsovedenski.galleryonsteroids

import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.medialist.*
import com.tsovedenski.galleryonsteroids.services.MediaService
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class MediaListPresenterTest {

    private val view: MediaListContract.View = mockk(relaxed = true)

    private val model: MediaListContract.ViewModel = mockk(relaxed = true)

    private val contextProvider: CoroutineContextProvider = mockk()

    private val service: MediaService = mockk(relaxed = true)

    private val adapter: MediaListAdapter = mockk(relaxed = true)

    private val presenter = MediaListPresenter(
        view,
        model,
        service,
        adapter,
        contextProvider
    )

    @BeforeEach
    fun setup() {
        clearAllMocks()
        every { contextProvider.provide() } returns Dispatchers.Unconfined
    }

    @Test
    fun `changes view type`() {
        val selected = ViewType.Card

        presenter.onChanged(MediaListEvent.ChangeViewType(selected))

        verify { view.setViewType(selected) }
        verify { adapter.viewType = selected }
        verify { model.viewType = selected }
    }

    @Test
    fun `loads items on resume`() {
        val list = dummyItems()
        coEvery { service.getMedia() } returns list

        presenter.onChanged(MediaListEvent.OnResume)

        verify { adapter.submitList(list) }
    }

    @Test
    fun `opens viewer on selected item`() {
        val item = dummyItems()[0]

        presenter.onChanged(MediaListEvent.ItemSelected(item))

        verify { view.openViewer(item) }
    }

    @Test
    fun `deletes item`() {
        val list = dummyItems()
        val deleted = list[0]

        every { adapter.currentList } returns list

        presenter.onChanged(MediaListEvent.DeleteItem(deleted))

        coVerify { service.delete(deleted) }
        verify { adapter.submitList(list.drop(1)) }
    }

    private fun dummyItems(): List<Media> {
        return listOf(
            Media(title = "first", type = MediaType.Photo),
            Media(title = "second", type = MediaType.Video),
            Media(title = "third", type = MediaType.Audio)
        )
    }
}