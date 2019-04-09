package com.tsovedenski.galleryonsteroids.features.medialist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.application
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.features.common.hasPermissions
import com.tsovedenski.galleryonsteroids.features.common.requestPermissions
import com.tsovedenski.galleryonsteroids.theme
import kotlinx.android.synthetic.main.activity_media_list.*
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import javax.inject.Inject

class MediaListView : Fragment(), MediaListContract.View {

    @Inject lateinit var injector: MediaListInjector

    private val event = MutableLiveData<MediaListEvent>()

    private var menu: Menu? = null

    private val itemsScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> fab_create.show()
                else -> fab_create.hide()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        application.appComponent.inject(this)
        injector.attachPresenter(this)
        Timber.tag(MediaListView::class.java.simpleName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_media_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFab()
        items.clearOnScrollListeners()
        items.addOnScrollListener(itemsScrollListener)
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart")
        event.value = MediaListEvent.OnStart
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume")
        event.value = MediaListEvent.OnResume
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
        event.value = MediaListEvent.OnDestroy
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.media_list, menu)
        event.value = MediaListEvent.OnOptionsReady
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
                items.layoutManager = value.toLayoutManager()
                items.setBackgroundColor(resources.getColor(R.color.black, theme))
                menu?.findItem(R.id.viewtype_grid)?.isVisible = false
                menu?.findItem(R.id.viewtype_card)?.isVisible = true
            }
            ViewType.Card -> {
                items.layoutManager = value.toLayoutManager()
                items.setBackgroundColor(resources.getColor(R.color.lightGray, theme))
                menu?.findItem(R.id.viewtype_grid)?.isVisible = true
                menu?.findItem(R.id.viewtype_card)?.isVisible = false
            }
        }
    }

    override fun openCreator(type: MediaType) {
        findNavController().navigate(MediaListViewDirections.actionMediaListViewToCreatorView(type.asString))
    }

    override fun openViewer(media: Media) {
        fab_create.close()
        findNavController().navigate(MediaListViewDirections.actionMediaListViewToViewerView(media))
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

    private fun ViewType.toLayoutManager() = when (this) {
        ViewType.Grid -> GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        ViewType.Card -> LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    companion object {
        private const val RC_PERMISSIONS = 1
    }
}
