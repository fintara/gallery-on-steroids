package com.tsovedenski.galleryonsteroids.features.medialist

import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
interface MediaListContract {
    interface View {
        fun setObserver(observer: Observer<MediaListEvent>)
        fun setAdapter(adapter: MediaListAdapter)
        fun setViewType(value: ViewType)

        fun openCreator(type: MediaType)
        fun openViewer(media: Media)

        fun checkPermissions(vararg perms: String)
    }

    interface ViewModel {
        var isLoaded: Boolean
        var viewType: ViewType
    }
}

sealed class ViewType (val asInt: Int) {
    object Grid : ViewType(1) {
        override fun toString() = "Grid"
    }

    object Card : ViewType(2) {
        override fun toString() = "Card"
    }

    companion object {
        fun fromInt(value: Int): ViewType? = when (value) {
            Grid.asInt -> Grid
            Card.asInt -> Card
            else -> null
        }
    }
}