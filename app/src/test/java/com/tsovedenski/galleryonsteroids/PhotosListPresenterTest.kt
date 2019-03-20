package com.tsovedenski.galleryonsteroids

import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.features.photoslist.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class PhotosListPresenterTest {

    private val view: PhotosListContract.View = mockk(relaxed = true)

    private val model: PhotosListContract.ViewModel = mockk(relaxed = true)

    private val contextProvider: CoroutineContextProvider = mockk()

    private val presenter = PhotosListPresenter(
        view,
        model,
        contextProvider
    )

    @BeforeEach
    fun setup() {
        clearAllMocks()
        every { contextProvider.provide() } returns Dispatchers.Unconfined
    }
}