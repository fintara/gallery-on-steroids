package com.tsovedenski.galleryonsteroids.features.form

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.GlideApp
import com.tsovedenski.galleryonsteroids.MyApplication
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.showToast
import kotlinx.android.synthetic.main.activity_details.*
import timber.log.Timber
import javax.inject.Inject

class FormActivity : AppCompatActivity(), FormContract.View {

    @Inject lateinit var injector: FormInjector
    private val event = MutableLiveData<FormEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        Timber.tag(FormActivity::class.java.name)
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

        event.value = FormEvent.OnStart(media)
    }

    override fun onResume() {
        super.onResume()
        event.value = FormEvent.OnResume
    }

    override fun onDestroy() {
        super.onDestroy()
        event.value = FormEvent.OnDestroy
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save) {
            event.value = FormEvent.Save(media_title.text.toString())
            return true
        }

        return false
    }

    override fun setThumbnail(media: Media) {
        GlideApp
            .with(this)
            .load(media.path)
            .into(media_thumbnail)
    }

    override fun setObserver(observer: Observer<FormEvent>) {
        event.observeForever(observer)
    }

    override fun close() {
        finish()
    }
}