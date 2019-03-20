package com.tsovedenski.galleryonsteroids.common

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
interface CoroutineContextProvider {
    fun provide(): CoroutineContext
}

object CoroutineContextProviderImpl : CoroutineContextProvider {
    override fun provide(): CoroutineContext = Dispatchers.Main
}