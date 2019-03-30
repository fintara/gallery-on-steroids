package com.tsovedenski.galleryonsteroids.features.medialist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.common.createDiffCallback
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import kotlinx.android.synthetic.main.media_row.view.*

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class MediaListAdapter (
    private val event: MutableLiveData<MediaListEvent> = MutableLiveData()
) : ListAdapter<Media, MediaListAdapter.ViewHolder>(diffCallback) {

    fun setObserver(observer: Observer<MediaListEvent>) = event.observeForever(observer)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.media_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

//        holder.thumbnail.setImageResource(android.R)
        holder.title.text = "${item.title} (${item.id})"
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        val title: TextView = view.findViewById(R.id.title)
    }
}

private val diffCallback = createDiffCallback<Media> { it.id }