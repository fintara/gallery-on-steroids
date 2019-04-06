package com.tsovedenski.galleryonsteroids.features.common

import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.common.CoroutineContextProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
abstract class Presenter <T> (
    coroutineContextProvider: CoroutineContextProvider
) : CoroutineScope, Observer<T> {

    private val job = Job()
    override val coroutineContext by lazy { coroutineContextProvider.provide() + job }

    protected open fun onDestroy() {
        job.cancel()
    }
}