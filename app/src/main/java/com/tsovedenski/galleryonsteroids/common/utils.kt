package com.tsovedenski.galleryonsteroids.common

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
fun <T> createDiffCallback(selector: (T) -> Any): DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = selector(oldItem) == selector(newItem)
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = selector(oldItem) == selector(newItem)
}