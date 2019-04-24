package com.tsovedenski.galleryonsteroids.common

import androidx.lifecycle.MutableLiveData
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Tsvetan Ovedenski on 24/04/19.
 */
fun <T> mutableLiveData(initValue: T) = mutableLiveData { initValue }

fun <T> mutableLiveData(initializer: () -> T) = mutableLiveData(initializer, {})

fun <T> mutableLiveData(initializer: () -> T, onSetValue: (T) -> Unit) = object : ReadWriteProperty<Any?, T> {
    private val state by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData<T>().apply { value = initializer() }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return state.value!!
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        onSetValue(value)
        state.value = value
    }
}

fun <T> mutableLiveData() = object : ReadWriteProperty<Any?, T?> {
    private val state by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData<T>()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return state.value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        state.value = value
    }
}