package com.tsovedenski.galleryonsteroids

import android.app.Application
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.google.firebase.FirebaseApp
import com.tsovedenski.galleryonsteroids.dagger.AppComponent
import com.tsovedenski.galleryonsteroids.dagger.DaggerAppComponent
import com.tsovedenski.galleryonsteroids.dagger.modules.AppModule
import timber.log.Timber
import timber.log.Timber.DebugTree


class MyApplication : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

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