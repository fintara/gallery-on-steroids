package com.tsovedenski.galleryonsteroids.dagger.modules

import android.content.Context
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import com.tsovedenski.galleryonsteroids.features.creator.CreatorInjector
import com.tsovedenski.galleryonsteroids.features.form.FormInjector
import com.tsovedenski.galleryonsteroids.features.medialist.MediaListInjector
import com.tsovedenski.galleryonsteroids.features.photoeditor.PhotoEditorInjector
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerTypeInjector
import com.tsovedenski.galleryonsteroids.services.ImageLabeler
import com.tsovedenski.galleryonsteroids.services.MediaService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
@Module
class InjectorModule {

    @Provides
    @Singleton
    fun provideMediaListInjector(
        context: Context,
        mediaService: MediaService,
        coroutineContextProvider: CoroutineContextProvider
    ) = MediaListInjector(context, mediaService, coroutineContextProvider)

    @Provides
    @Singleton
    fun provideCreatorInjector(
        coroutineContextProvider: CoroutineContextProvider
    ) = CreatorInjector(coroutineContextProvider)

    @Provides
    @Singleton
    fun provideFormInjector(
        mediaService: MediaService,
        imageLabeler: ImageLabeler,
        coroutineContextProvider: CoroutineContextProvider
    ) = FormInjector(mediaService, imageLabeler, coroutineContextProvider)

    @Provides
    @Singleton
    fun provideViewerTypeInjector(
        imageLabeler: ImageLabeler,
        coroutineContextProvider: CoroutineContextProvider
    ) = ViewerTypeInjector(imageLabeler, coroutineContextProvider)

    @Provides
    @Singleton
    fun providePhotoEditorInjector(
        service: MediaService,
        coroutineContextProvider: CoroutineContextProvider
    ) = PhotoEditorInjector(service, coroutineContextProvider)
}