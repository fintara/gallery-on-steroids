package com.tsovedenski.galleryonsteroids.dagger

import com.tsovedenski.galleryonsteroids.dagger.modules.AppModule
import com.tsovedenski.galleryonsteroids.dagger.modules.DatabaseModule
import com.tsovedenski.galleryonsteroids.dagger.modules.MediaListModule
import com.tsovedenski.galleryonsteroids.features.medialist.MediaListActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, MediaListModule::class])
interface AppComponent {
    fun inject(activity: MediaListActivity)
}