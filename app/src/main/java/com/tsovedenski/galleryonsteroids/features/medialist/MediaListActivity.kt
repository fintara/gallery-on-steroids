package com.tsovedenski.galleryonsteroids.features.medialist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsovedenski.galleryonsteroids.MyApplication
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.common.hasPermissions
import com.tsovedenski.galleryonsteroids.features.common.requestPermissions
import com.tsovedenski.galleryonsteroids.features.creator.CreatorActivity
import com.tsovedenski.galleryonsteroids.features.viewer.ViewerActivity
import kotlinx.android.synthetic.main.activity_media_list.*
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

class MediaListActivity : AppCompatActivity(), MediaListContract.View {

    @Inject lateinit var injector: MediaListInjector

    private val event = MutableLiveData<MediaListEvent>()

    private var menu: Menu? = null

    private val gridLayoutManager by lazy {
        GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
    }

    private val cardLayoutManager by lazy {
        LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.media_list, menu)
        event.value = MediaListEvent.OnResume
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val next = when (item.itemId) {
            R.id.viewtype_card -> {
                MediaListEvent.ChangeViewType(ViewType.Card)
            }
            R.id.viewtype_grid -> {
                MediaListEvent.ChangeViewType(ViewType.Grid)
            }
            else -> null
        }

        next?.let { event.value = it }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun setObserver(observer: Observer<MediaListEvent>) {
        event.observeForever(observer)
    }

    override fun setAdapter(adapter: MediaListAdapter) {
        items.adapter = adapter
    }

    override fun setViewType(value: ViewType) {
        when (value) {
            ViewType.Grid -> {
                items.layoutManager = gridLayoutManager
                items.setBackgroundColor(resources.getColor(R.color.black, theme))
                menu?.findItem(R.id.viewtype_grid)?.isVisible = false
                menu?.findItem(R.id.viewtype_card)?.isVisible = true
            }
            ViewType.Card -> {
                items.layoutManager = cardLayoutManager
                items.setBackgroundColor(resources.getColor(R.color.lightGray, theme))
                menu?.findItem(R.id.viewtype_grid)?.isVisible = true
                menu?.findItem(R.id.viewtype_card)?.isVisible = false
            }
        }
    }

    override fun openCreator(type: MediaType) {
        fab_create.close()
        val intent = Intent(this, CreatorActivity::class.java).apply {
            putExtra("type", type.asString)
        }
        startActivity(intent)
    }

    override fun openViewer(media: Media) {
        fab_create.close()
        val intent = Intent(this, ViewerActivity::class.java).apply {
            putExtra(ViewerActivity.INTENT_EXTRA_MEDIA, media)
        }
        startActivity(intent)
    }

    override fun checkPermissions(vararg perms: String) {
        if (hasPermissions(*perms)) {
            return
        }

        requestPermissions(R.string.need_read_permission, RC_PERMISSIONS, *perms)
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
        private const val RC_PERMISSIONS = 1
    }
}
