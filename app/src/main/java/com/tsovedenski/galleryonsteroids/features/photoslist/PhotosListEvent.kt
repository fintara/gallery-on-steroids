package com.tsovedenski.galleryonsteroids.features.photoslist

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
sealed class PhotosListEvent {
    object OnStart : PhotosListEvent()
    object OnResume : PhotosListEvent()
    object OnDestroy : PhotosListEvent()
}