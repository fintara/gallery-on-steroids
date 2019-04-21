package com.tsovedenski.galleryonsteroids.features.common

import android.os.Bundle

/**
 * Created by Tsvetan Ovedenski on 21/04/19.
 */

/**
 * @url https://medium.com/google-developer-experts/using-navigation-architecture-component-in-a-large-banking-app-ac84936a42c2
 */
interface NavigationResult <in T> {
    fun onNavigationResult(payload: T)
}