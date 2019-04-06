package com.tsovedenski.galleryonsteroids.features.form

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.GlideApp
import com.tsovedenski.galleryonsteroids.MyApplication
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType
import com.tsovedenski.galleryonsteroids.showToast
import kotlinx.android.synthetic.main.activity_form.*
import timber.log.Timber
import javax.inject.Inject

class FormActivity : AppCompatActivity(), FormContract.View {

    @Inject lateinit var injector: FormInjector
    private val event = MutableLiveData<FormEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

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

        title = "${resources.getString(R.string.save_new)} ${resources.getString(when (media.type) {
            MediaType.Photo -> R.string.photo
            MediaType.Video -> R.string.video
            MediaType.Audio -> R.string.voice
        })}"
    }

    override fun onResume() {
        super.onResume()
        event.value = FormEvent.OnResume
    }

    override fun onPause() {
        event.value = FormEvent.OnPause
        super.onPause()
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

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.discard_title)

            setPositiveButton(R.string.discard) { _, _ ->
                event.value = FormEvent.Discard
                super.onBackPressed()
            }

            setNegativeButton(R.string.stay) { dialog, _ ->
                dialog.cancel()
            }
        }.show()
    }

    override fun setThumbnail(media: Media) {
        GlideApp
            .with(this)
            .load(media.path)
            .into(media_thumbnail)
    }

    override fun showMessage(resId: Int) {
        showToast(resId)
    }

    override fun setObserver(observer: Observer<FormEvent>) {
        event.observeForever(observer)
    }

    override fun close() {
        finish()
    }
}