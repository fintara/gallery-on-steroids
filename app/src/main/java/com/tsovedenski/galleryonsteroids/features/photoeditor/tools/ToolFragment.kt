package com.tsovedenski.galleryonsteroids.features.photoeditor.tools

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.tsovedenski.galleryonsteroids.R
import com.tsovedenski.galleryonsteroids.features.common.navigateBackWithResult
import com.tsovedenski.galleryonsteroids.features.photoeditor.PhotoModification
import timber.log.Timber

/**
 * Created by Tsvetan Ovedenski on 21/04/19.
 */
abstract class ToolFragment : Fragment() {

    protected abstract val layoutId: Int

    protected abstract val modification: PhotoModification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, OnBackPressedCallback {
            Timber.i("Going back from ToolFragment")
            navigateBackWithResult(null)
            true
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tool, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.confirm) {
            navigateBackWithResult(modification)
        }

        return super.onOptionsItemSelected(item)
    }
}