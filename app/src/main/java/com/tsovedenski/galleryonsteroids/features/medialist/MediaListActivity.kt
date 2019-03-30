package com.tsovedenski.galleryonsteroids.features.medialist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsovedenski.galleryonsteroids.MyApplication
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.creator.CreatorActivity
import kotlinx.android.synthetic.main.activity_media_list.*
import javax.inject.Inject

class MediaListActivity : AppCompatActivity(), MediaListContract.View {

    @Inject lateinit var injector: MediaListInjector
    private val event = MutableLiveData<MediaListEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_list)

        (application as MyApplication).appComponent.inject(this)

        injector.attachPresenter(this)

        initFab()
    }

    override fun onStart() {
        super.onStart()
        event.value = MediaListEvent.OnStart
    }

    override fun onResume() {
        super.onResume()
        event.value = MediaListEvent.OnResume
    }

    override fun onDestroy() {
        super.onDestroy()
        event.value = MediaListEvent.OnDestroy
    }

    override fun setObserver(observer: Observer<MediaListEvent>) {
        event.observeForever(observer)
    }

    override fun setAdapter(adapter: MediaListAdapter) {
        items.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        items.adapter = adapter
    }

    override fun openCreator(type: MediaType) {
        fab_create.close()
        val intent = Intent(this, CreatorActivity::class.java).apply {
            putExtra("type", type.asString)
        }
        startActivity(intent)
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
}
