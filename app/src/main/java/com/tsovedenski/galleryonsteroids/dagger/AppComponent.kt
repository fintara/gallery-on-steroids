package com.tsovedenski.galleryonsteroids.dagger

import com.tsovedenski.galleryonsteroids.dagger.modules.AppModule
import com.tsovedenski.galleryonsteroids.dagger.modules.DatabaseModule
import com.tsovedenski.galleryonsteroids.dagger.modules.InjectorModule
import com.tsovedenski.galleryonsteroids.features.creator.CreatorView
import com.tsovedenski.galleryonsteroids.features.form.FormView
import com.tsovedenski.galleryonsteroids.features.medialist.MediaListView
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerView
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, InjectorModule::class])
interface AppComponent {
    fun inject(view: MediaListView)
    fun inject(view: CreatorView)
    fun inject(view: FormView)
    fun inject(view: ViewerView)
}