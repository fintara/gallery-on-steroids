package com.tsovedenski.galleryonsteroids

import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.features.medialist.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.BeforeEach

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class MediaListPresenterTest {

    private val view: MediaListContract.View = mockk(relaxed = true)

    private val model: MediaListContract.ViewModel = mockk(relaxed = true)

    private val contextProvider: CoroutineContextProvider = mockk()

    private val presenter = MediaListPresenter(
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