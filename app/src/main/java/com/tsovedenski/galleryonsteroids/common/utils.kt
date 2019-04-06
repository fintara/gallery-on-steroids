package com.tsovedenski.galleryonsteroids.common

import androidx.recyclerview.widget.DiffUtil

/**
 * Created by Tsvetan Ovedenski on 30/03/19.
 */
fun <T> createDiffCallback(selector: (T) -> Any): DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = selector(oldItem) == selector(newItem)
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = selector(oldItem) == selector(newItem)
}

fun Long?.toDurationString(): String {
    if (this == null) {
        return "00:00"
    }

    val minutes = (this / 1000 / 60)
    var minutesStr = minutes.toString()
    if (minutesStr.length == 1) minutesStr = "0$minutesStr"

    val seconds = (this / 1000 - minutes * 60)
    var secondsStr = seconds.toString()
    if (secondsStr.length == 1) secondsStr = "0$secondsStr"
    return "$minutesStr:$secondsStr"
}