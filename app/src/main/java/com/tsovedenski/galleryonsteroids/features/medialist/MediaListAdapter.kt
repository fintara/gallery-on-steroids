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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tsovedenski.galleryonsteroids.GlideApp
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.common.createDiffCallback
import com.tsovedenski.galleryonsteroids.common.prettyFormat
import com.tsovedenski.galleryonsteroids.common.toDurationString
import com.tsovedenski.galleryonsteroids.domain.entities.Media
import com.tsovedenski.galleryonsteroids.domain.entities.MediaType

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
            ViewType.Grid.asInt -> Pair(ViewHolder::GridViewHolder, R.layout.medialist_item_grid)
            ViewType.Card.asInt -> Pair(ViewHolder::CardViewHolder, R.layout.medialist_item_card)
            else -> throw RuntimeException("Unknown viewType: $viewType")
        }
        return holder(inflater.inflate(layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindTo(item, position, event, context)
    }

    sealed class ViewHolder(protected val view: View) : RecyclerView.ViewHolder(view) {
        private val thumbnail: ImageView = view.findViewById(R.id.media_thumbnail)
        private val type: ImageView = view.findViewById(R.id.media_type)

        open fun bindTo(
            item: Media,
            position: Int,
            event: MutableLiveData<MediaListEvent>,
            context: Context
        ) {
            GlideApp
                .with(context)
                .load(item.thumbnailPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(thumbnail)

            val typeIconId = when (item.type) {
                MediaType.Photo -> R.drawable.camera
                MediaType.Video -> R.drawable.video
                MediaType.Audio -> R.drawable.microphone
            }

            GlideApp
                .with(context)
                .load(typeIconId)
                .fitCenter()
                .into(type)

            view.setOnClickListener {
                event.value = MediaListEvent.ItemSelected(item)
            }

            view.setOnLongClickListener {
                event.value = MediaListEvent.OptionsSelected(item)
                true
            }
        }

        class GridViewHolder(view: View) : ViewHolder(view)

        class CardViewHolder(view: View) : ViewHolder(view) {
            private val title: TextView = view.findViewById(R.id.media_title)
            private val date: TextView = view.findViewById(R.id.media_date)
            private val duration: TextView = view.findViewById(R.id.media_duration)

            override fun bindTo(item: Media, position: Int, event: MutableLiveData<MediaListEvent>, context: Context) {
                super.bindTo(item, position, event, context)

                title.text = item.title
                date.text = item.createdAt.prettyFormat()

                if (item.type == MediaType.Photo || item.duration == null) {
                    duration.visibility = View.GONE
                } else {
                    duration.visibility = View.VISIBLE
                    duration.text = item.duration.toDurationString()
                }
            }
        }
    }
}

private val diffCallback = createDiffCallback<Media> { it.id }