package com.tsovedenski.galleryonsteroids.dagger.modules

import android.content.Context
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProviderImpl
import com.tsovedenski.galleryonsteroids.domain.repositories.MediaRepository
import com.tsovedenski.galleryonsteroids.services.MediaService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
@Module
class AppModule (private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideMediaService(
        mediaRepository: MediaRepository
    ): MediaService = MediaService(mediaRepository)

    @Provides
    @Singleton
    fun provideCoroutineContextProvider(): CoroutineContextProvider =
        CoroutineContextProviderImpl

}