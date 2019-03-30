package com.tsovedenski.galleryonsteroids.features.medialist

import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
interface MediaListContract {
    interface View {
        fun setObserver(observer: Observer<MediaListEvent>)
        fun setAdapter(adapter: MediaListAdapter)
    }

    interface ViewModel {
        fun isLoaded(): Boolean
        fun setLoaded(value: Boolean)
    }
}
