package com.tsovedenski.galleryonsteroids

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_root.*
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * Created by Tsvetan Ovedenski on 08/04/19.
 */
class RootActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        window.decorView.setBackgroundColor(resources.getColor(R.color.black, theme))

        Navigation.findNavController(this, R.id.nav_host)
    }
}