package com.tsovedenski.galleryonsteroids.features.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.MyApplication
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.showToast
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity(), DetailsContract.View {

    @Inject lateinit var injector: DetailsInjector
    private val event = MutableLiveData<DetailsEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        (application as MyApplication).appComponent.inject(this)

        injector.attachPresenter(this)
    }

    override fun onStart() {
        super.onStart()

        val media = intent.getParcelableExtra<Media?>("media")

        if (media == null) {
            showToast("Media cannot be null")
            finish()
            return
        }

        event.value = DetailsEvent.OnStart(media)
    }

    override fun onResume() {
        super.onResume()
        event.value = DetailsEvent.OnResume
    }

    override fun onDestroy() {
        super.onDestroy()
        event.value = DetailsEvent.OnDestroy
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            event.value = DetailsEvent.Save(media_title.text.toString())
            return true
        }

        return false
    }

    override fun setObserver(observer: Observer<DetailsEvent>) {
        event.observeForever(observer)
    }
}