package com.tsovedenski.galleryonsteroids.features.photoeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.features.common.application
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Tsvetan Ovedenski on 20/04/2019.
 */
class PhotoEditorView : Fragment(), PhotoEditorContract.View {

    @Inject lateinit var injector: PhotoEditorInjector

    private val event = MutableLiveData<PhotoEditorEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application.appComponent.inject(this)
        injector.attachPresenter(this)
        Timber.tag(PhotoEditorView::class.java.simpleName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photoeditor, container, false)
    }

    override fun setObserver(observer: Observer<PhotoEditorEvent>) {
        event.observeForever(observer)
    }
}