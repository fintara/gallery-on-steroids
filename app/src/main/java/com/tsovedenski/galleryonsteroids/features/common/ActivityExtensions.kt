package com.tsovedenski.galleryonsteroids.features.common

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Created by Tsvetan Ovedenski on 10/04/2019.
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