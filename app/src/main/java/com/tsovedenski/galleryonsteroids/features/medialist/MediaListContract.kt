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

        fun checkPermissions(vararg perms: String)
    }

    interface ViewModel {
        fun isLoaded(): Boolean
        fun setLoaded(value: Boolean)
        fun getViewType(): ViewType
        fun setViewType(value: ViewType)
    }
}

sealed class ViewType (val asInt: Int) {
    object Grid : ViewType(1)
    object Card : ViewType(2)
}