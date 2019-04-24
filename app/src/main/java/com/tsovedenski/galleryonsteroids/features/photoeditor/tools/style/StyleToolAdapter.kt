package com.tsovedenski.galleryonsteroids.features.photoeditor.tools.style

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.tsovedenski.galleryonsteroids.GlideApp
import com.tsovedenski.galleryonsteroids.R

/**
 * Created by Tsvetan Ovedenski on 24/04/19.
 */
class StyleToolAdapter (
    private val styles: List<Style>,
    private val imageUri: String,
    private val context: Context,
    private val onSelected: (Style) -> Unit
) : RecyclerView.Adapter<StyleToolAdapter.ViewHolder>() {

    private fun createSpinner() = CircularProgressDrawable(context).apply {
        strokeWidth = 3f
        centerRadius = 30f
        setTint(context.resources.getColor(R.color.white, null))
        start()
    }

    override fun getItemCount() = styles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(
                R.layout.photoeditor_tool_style_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val style = styles[position]
        holder.apply {
            GlideApp.with(context)
                .asBitmap()
                .load(imageUri)
                .placeholder(createSpinner())
                .centerCrop()
                .transform(style.transformation)
                .into(thumbnail)

            styleName.text = style.name

            view.setOnClickListener { onSelected(style) }
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        val styleName: TextView = view.findViewById(R.id.styleName)
    }
}