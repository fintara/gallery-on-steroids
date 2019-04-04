package com.tsovedenski.galleryonsteroids.features.medialist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tsovedenski.galleryonsteroids.GlideApp
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.common.createDiffCallback
import com.tsovedenski.galleryonsteroids.domain.entities.Media

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
class MediaListAdapter (
    private val context: Context,
    private val event: MutableLiveData<MediaListEvent> = MutableLiveData()
) : ListAdapter<Media, MediaListAdapter.ViewHolder>(diffCallback) {

    var viewType: ViewType = ViewType.Grid
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun setObserver(observer: Observer<MediaListEvent>) = event.observeForever(observer)

    override fun getItemViewType(position: Int): Int = viewType.asInt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val (holder, layout) = when (viewType) {
            ViewType.Grid.asInt -> Pair(ViewHolder::GridViewHolder, R.layout.media_row_grid)
            ViewType.Card.asInt -> Pair(ViewHolder::CardViewHolder, R.layout.media_row_card)
            else -> throw RuntimeException("Unknown viewType: $viewType")
        }
        return holder(inflater.inflate(layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindTo(item, position, event, context)
    }

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bindTo(
            item: Media,
            position: Int,
            event: MutableLiveData<MediaListEvent>,
            context: Context
        )

        class GridViewHolder(view: View) : ViewHolder(view) {
            override fun bindTo(item: Media, position: Int, event: MutableLiveData<MediaListEvent>, context: Context) {

            }
        }

        class CardViewHolder(view: View) : ViewHolder(view) {
            private val thumbnail: ImageView = view.findViewById(R.id.media_thumbnail)
            private val title: TextView = view.findViewById(R.id.media_title)

            override fun bindTo(item: Media, position: Int, event: MutableLiveData<MediaListEvent>, context: Context) {
                GlideApp
                    .with(context)
                    .load(item.path)
                    .into(thumbnail)
                title.text = "${item.title} (${item.id})"
            }
        }
    }
}

private val diffCallback = createDiffCallback<Media> { it.id }