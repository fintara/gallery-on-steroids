package com.tsovedenski.galleryonsteroids.features.photoeditor.tools.tune

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tsovedenski.galleryonsteroids.R
import timber.log.Timber

/**
 * Created by Tsvetan Ovedenski on 24/04/19.
 */
class TuneToolAdapter (
    private val bars: List<Bar>,
    private val onSliderChange: (List<Bar>) -> Unit
) : RecyclerView.Adapter<TuneToolAdapter.ViewHolder>() {

    override fun getItemCount(): Int = bars.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(
                R.layout.tool_tune_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bar = bars[position]

        holder.apply {
            label.text = bar.toString()
            seekbar.min = bar.min
            seekbar.max = bar.max
            seekbar.setProgress(bar.current, false)
        }

        holder.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                bar.current = progress
                holder.label.text = bar.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Timber.i("Finished ${bar.label} as ${bar.current}")

                holder.label.text = bar.toString()

                onSliderChange(bars)
            }
        })
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.bar_label)
        val seekbar: SeekBar = view.findViewById(R.id.seekbar)
    }
}