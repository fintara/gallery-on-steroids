package com.tsovedenski.galleryonsteroids

import android.app.Application
import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.tsovedenski.galleryonsteroids.dagger.AppComponent
import com.tsovedenski.galleryonsteroids.dagger.DaggerAppComponent
import com.tsovedenski.galleryonsteroids.dagger.modules.AppModule
import timber.log.Timber.DebugTree
import timber.log.Timber



class MyApplication : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}

@GlideModule
class MyGlideModule : AppGlideModule()