package com.tsovedenski.galleryonsteroids.features.medialist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import kotlinx.android.synthetic.main.fragment_media_list.*

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
class MediaListView : Fragment(), MediaListContract.View {
    private val event = MutableLiveData<MediaListEvent>()

    override fun setObserver(observer: Observer<MediaListEvent>) {
        event.observeForever(observer)
    }

    override fun setAdapter(adapter: MediaListAdapter) {
        items.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        items.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_media_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        event.value = MediaListEvent.OnStart
        initFab()
    }

    override fun onResume() {
        super.onResume()
        event.value = MediaListEvent.OnResume
    }

    override fun onDestroy() {
        super.onDestroy()
        event.value = MediaListEvent.OnDestroy
    }

    private fun initFab() {
        fab_create.inflate(R.menu.fab)
        fab_create.setOnActionSelectedListener {
            when (it.id) {
                R.id.fab_photo -> { event.value = MediaListEvent.CreatePhoto; true }
                R.id.fab_video -> { event.value = MediaListEvent.CreateVideo; true }
                R.id.fab_audio -> { event.value = MediaListEvent.CreateAudio; true }
                else -> false
            }
        }
    }

    companion object {
        fun newInstance() = MediaListView()
    }
}