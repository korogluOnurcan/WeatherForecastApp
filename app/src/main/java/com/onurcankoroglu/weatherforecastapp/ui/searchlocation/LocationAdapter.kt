package com.onurcankoroglu.weatherforecastapp.ui.searchlocation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onurcankoroglu.weatherforecastapp.data.model.Location
import com.onurcankoroglu.weatherforecastapp.databinding.ViewLocationBinding

class LocationAdapter(private val listener: InteractionListener) :
    ListAdapter<Location, RecyclerView.ViewHolder>(LocationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LocationViewHolder(
            listener,
            ViewLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LocationViewHolder).bind(getItem(position))
    }

    class LocationViewHolder(
        private val listener: InteractionListener,
        private val binding: ViewLocationBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.mwLocation?.let { mwLocation ->
                    listener.onItemSelected(mwLocation.woeid)
                }
            }
        }

        fun bind(item: Location) {
            binding.apply {
                mwLocation = item
                executePendingBindings()
            }
        }
    }

    private class LocationDiffCallback : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.woeid == newItem.woeid
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }
    }

    interface InteractionListener {
        fun onItemSelected(mwLocationID: Int)
    }
}