package com.tsovedenski.galleryonsteroids

import android.app.Application
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

class MyApplication : Application()

@GlideModule
class MyGlideModule : AppGlideModule()