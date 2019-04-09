package com.tsovedenski.galleryonsteroids

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation

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