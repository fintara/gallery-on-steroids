package com.tsovedenski.galleryonsteroids.features.common

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tsovedenski.galleryonsteroids.R
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

/**
 * Created by Tsvetan Ovedenski on 04/04/19.
 */
fun AppCompatActivity.hasPermissions(vararg perms: String): Boolean {
    return EasyPermissions.hasPermissions(this, *perms)
}

fun Fragment.hasPermissions(vararg perms: String): Boolean {
    return EasyPermissions.hasPermissions(requireContext(), *perms)
}

fun AppCompatActivity.requestPermissions(@StringRes rationaleResId: Int, requestCode: Int, vararg perms: String) {
    EasyPermissions.requestPermissions(
        PermissionRequest.Builder(this, requestCode, *perms)
            .setRationale(rationaleResId)
            .setPositiveButtonText(R.string.ok)
            .setNegativeButtonText(R.string.cancel)
            .build()
    )
}

fun Fragment.requestPermissions(@StringRes rationaleResId: Int, requestCode: Int, vararg perms: String) {
    EasyPermissions.requestPermissions(
        PermissionRequest.Builder(this, requestCode, *perms)
            .setRationale(rationaleResId)
            .setPositiveButtonText(R.string.ok)
            .setNegativeButtonText(R.string.cancel)
            .build()
    )
}