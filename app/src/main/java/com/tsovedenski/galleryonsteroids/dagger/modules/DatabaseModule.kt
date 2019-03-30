package com.tsovedenski.galleryonsteroids.dagger.modules

import android.content.Context
import androidx.room.Room
import com.tsovedenski.galleryonsteroids.domain.AppDatabase
import com.tsovedenski.galleryonsteroids.domain.AppDatabaseImpl
import com.tsovedenski.galleryonsteroids.domain.repositories.MediaRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabaseImpl::class.java, "app")
        .build()

    @Provides
    fun provideMediaRepository(database: AppDatabase): MediaRepository =
        database.getMediaRepository()

}