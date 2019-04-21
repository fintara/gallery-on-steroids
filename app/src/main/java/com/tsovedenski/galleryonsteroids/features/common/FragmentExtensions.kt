package com.tsovedenski.galleryonsteroids.features.common

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.tsovedenski.galleryonsteroids.MyApplication
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.features.form.FormEvent
import timber.log.Timber
import kotlin.properties.Delegates

/**
 * Created by Tsvetan Ovedenski on 10/04/2019.
 */
fun Fragment.setFragment(fragment: Fragment, tag: String, containerViewId: Int) =
    childFragmentManager.beginTransaction()
        .replace(containerViewId, fragment, tag)
        .commitNowAllowingStateLoss()

fun Fragment.showToast(@StringRes resId: Int) {
    Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(value: String) {
    Toast.makeText(activity, value, Toast.LENGTH_SHORT).show()
}

private val fullscreenFlags = listOf(
    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN,
    View.SYSTEM_UI_FLAG_FULLSCREEN,
    View.SYSTEM_UI_FLAG_LAYOUT_STABLE,
    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION,
    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION,
    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
).reduce(Int::or)

fun Fragment.enterFullscreen() {
    window.decorView.systemUiVisibility = fullscreenFlags
    window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

fun Fragment.exitFullscreen() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

fun Fragment.hideKeyboard() {
    val inputMethodManager = requireActivity()
        .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Fragment.setTitle(@StringRes resId: Int) {
    requireActivity().setTitle(resId)
}

fun Fragment.resetTitle() {
    requireActivity().setTitle(R.string.app_name)
}

@Suppress("UNCHECKED_CAST")
fun <T> Fragment.navigateBackWithResult(payload: T) {
    Timber.i("Navigating back with result: $payload")
    val fm = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host)?.childFragmentManager
    lateinit var backStackListener: FragmentManager.OnBackStackChangedListener
    backStackListener = FragmentManager.OnBackStackChangedListener {
        (fm?.fragments?.get(0) as? NavigationResult<T>)?.onNavigationResult(payload)
        fm?.removeOnBackStackChangedListener(backStackListener)
    }
    fm?.addOnBackStackChangedListener(backStackListener)
    findNavController().popBackStack()
}

fun Fragment.discardDialog(action: () -> Unit) {
    AlertDialog.Builder(requireContext()).apply {
        setTitle(R.string.discard_title)

        setPositiveButton(R.string.discard) { _, _ ->
            action()
        }

        setNegativeButton(R.string.stay) { dialog, _ ->
            dialog.cancel()
        }
    }.show()
}

val Fragment.application: MyApplication get() = requireActivity().application as MyApplication
val Fragment.theme: Resources.Theme get() = requireActivity().theme
val Fragment.window: Window get() = requireActivity().window
val Fragment.actionBar: ActionBar? get() = (requireActivity() as AppCompatActivity).supportActionBar