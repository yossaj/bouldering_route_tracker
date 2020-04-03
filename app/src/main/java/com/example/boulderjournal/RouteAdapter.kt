package com.example.boulderjournal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.boulderjournal.Utils.Utilities
import com.example.boulderjournal.data.RouteEntry
import com.example.boulderjournal.databinding.RouteInputLayoutBinding


class RouteAdapter(private val mItemClickListener: ItemClickListener) :  ListAdapter<RouteEntry, RouteAdapter.RouteViewHolder>(RouteEntryDiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RouteInputLayoutBinding.inflate(layoutInflater,parent, false)

        return RouteViewHolder(binding, parent.context)
    }

    fun getRouteByPosition(position: Int) : RouteEntry{
        return getItem(position)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val routeEntry = getItem(position)
        holder.bindRoute(routeEntry)
    }

    interface ItemClickListener {
        fun onItemClickListener(itemId: Int)
    }

    inner class RouteViewHolder(val binding: RouteInputLayoutBinding, val context: Context) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.item.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val elementId = getItem(adapterPosition).id
            mItemClickListener.onItemClickListener(elementId)
        }

        fun bindRoute(routeEntry: RouteEntry) {
            val routeName = routeEntry.routeName
            binding.routeNameMain.text = routeName
            val routeColor = routeEntry.routeColour
            val routeColourInt = Utilities.getColor(routeColor, context)
            binding.colorSwatchMain.setBackgroundColor(routeColourInt)
        }
    }

    class RouteEntryDiffCallBack() :
            DiffUtil.ItemCallback<RouteEntry>(){
        override fun areItemsTheSame(oldItem: RouteEntry, newItem: RouteEntry): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RouteEntry, newItem: RouteEntry): Boolean {
            return oldItem.equals(newItem)
        }
    }

}
