package com.tsovedenski.galleryonsteroids.common

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Created by Tsvetan Ovedenski on 10/04/2019.
 */
fun Instant.prettyFormat(): String = formatter.format(this)

private val formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm")
    .withZone(ZoneId.systemDefault())