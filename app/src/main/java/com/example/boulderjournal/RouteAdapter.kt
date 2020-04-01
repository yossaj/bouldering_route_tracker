package com.example.boulderjournal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boulderjournal.Utils.Utilities
import com.example.boulderjournal.data.RouteEntry
import com.example.boulderjournal.databinding.RouteInputLayoutBinding


class RouteAdapter(private val mItemClickListener: ItemClickListener) :  RecyclerView.Adapter<RouteAdapter.RouteViewHolder>(){
    var routes = listOf<RouteEntry>()
        set(routeEntries) {
            field = routeEntries
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RouteInputLayoutBinding.inflate(layoutInflater,parent, false)

        return RouteViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val routeEntry = routes[position]
        holder.bindRoute(routeEntry)
    }


    override fun getItemCount(): Int = routes.size

    interface ItemClickListener {
        fun onItemClickListener(itemId: Int)
    }

    inner class RouteViewHolder(val binding: RouteInputLayoutBinding, val context: Context) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.item.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val elementId = routes[adapterPosition].id
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

}
