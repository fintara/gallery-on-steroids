package com.tsovedenski.galleryonsteroids

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Created by Tsvetan Ovedenski on 10/03/19.
 */
fun AppCompatActivity.setFragment(fragment: Fragment, tag: String, containerViewId: Int) =
    supportFragmentManager.beginTransaction()
        .replace(containerViewId, fragment, tag)
        .commitNowAllowingStateLoss()

fun AppCompatActivity.showToast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.showToast(value: String) {
    Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(@StringRes resId: Int) {
    Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(value: String) {
    Toast.makeText(activity, value, Toast.LENGTH_SHORT).show()
}

fun Instant.prettyFormat(): String = formatter.format(this)

private val formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm")
    .withZone(ZoneId.systemDefault())