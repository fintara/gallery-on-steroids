package com.tsovedenski.galleryonsteroids.dagger

import com.tsovedenski.galleryonsteroids.dagger.modules.AppModule
import com.tsovedenski.galleryonsteroids.dagger.modules.DatabaseModule
import com.tsovedenski.galleryonsteroids.dagger.modules.InjectorModule
import com.tsovedenski.galleryonsteroids.features.creator.CreatorActivity
import com.tsovedenski.galleryonsteroids.features.form.FormActivity
import com.tsovedenski.galleryonsteroids.features.medialist.MediaListActivity
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
@Singleton
@Component(modules = [AppModule::class, DatabaseModule::class, InjectorModule::class])
interface AppComponent {
    fun inject(activity: MediaListActivity)
    fun inject(activity: CreatorActivity)
    fun inject(activity: FormActivity)
    fun inject(activity: ViewerActivity)
}