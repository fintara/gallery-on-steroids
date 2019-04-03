package com.tsovedenski.galleryonsteroids.dagger

import androidx.appcompat.app.AppCompatActivity
import com.tsovedenski.galleryonsteroids.dagger.modules.AppModule
import com.tsovedenski.galleryonsteroids.dagger.modules.DatabaseModule
import com.tsovedenski.galleryonsteroids.dagger.modules.InjectorModule
import com.tsovedenski.galleryonsteroids.features.creator.CreatorActivity
import com.tsovedenski.galleryonsteroids.features.details.DetailsActivity
import com.tsovedenski.galleryonsteroids.features.medialist.MediaListActivity
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
    fun inject(activity: DetailsActivity)
}