package com.tsovedenski.galleryonsteroids.dagger.modules

import android.content.Context
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.domain.repositories.MediaRepository
import com.tsovedenski.galleryonsteroids.features.medialist.MediaListInjector
import com.tsovedenski.galleryonsteroids.services.MediaService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
@Module
class MediaListModule {

    @Provides
    @Singleton
    fun provideInjector(
        mediaService: MediaService,
        coroutineContextProvider: CoroutineContextProvider
    ) = MediaListInjector(mediaService, coroutineContextProvider)

}