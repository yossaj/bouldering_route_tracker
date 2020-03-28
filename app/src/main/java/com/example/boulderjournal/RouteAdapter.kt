package com.example.boulderjournal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.boulderjournal.Utils.Utilities
import com.example.boulderjournal.data.RouteEntry


class RouteAdapter(private val mContext: Context, private val mItemClickListener: ItemClickListener) : RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {
    var routes = listOf<RouteEntry>()
        set(routeEntries) {
            field = routeEntries
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.route_input_layout, parent, false)

        return RouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val routeEntry = routes!![position]
        bindRoute(routeEntry, holder)
    }

    private fun bindRoute(routeEntry: RouteEntry, holder: RouteViewHolder) {
        val routeName = routeEntry.routeName
        holder.routeTitleMain.text = routeName
        val routeColor = routeEntry.routeColour
        val routeColourInt = Utilities.getColor(routeColor!!, mContext)
        holder.routeColourSwatch.setBackgroundColor(routeColourInt)
    }


    override fun getItemCount(): Int = routes.size

    interface ItemClickListener {
        fun onItemClickListener(itemId: Int)
    }


    inner class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var routeTitleMain: TextView
        var routeColourSwatch: View

        init {
            routeTitleMain = itemView.findViewById<View>(R.id.route_name_main) as TextView
            routeColourSwatch = itemView.findViewById(R.id.color_swatch_main) as View
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val elementId = routes!![adapterPosition].id
            mItemClickListener.onItemClickListener(elementId)
        }
    }


}
