package com.example.boulderjournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boulderjournal.data.RouteEntry;

import java.util.List;


public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private Context mContext;
    private List<RouteEntry> mRouteEntries;

    public RouteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.route_input_layout, parent, false);

        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder( RouteViewHolder holder, int position) {

        RouteEntry routeEntry = mRouteEntries.get(position);
        String routeName = routeEntry.getRouteName();

        holder.routeTitleMain.setText(routeName);
    }




    @Override
    public int getItemCount() {
        if (mRouteEntries == null) {
            return 0;
        }
        int count = mRouteEntries.size();
        return count;
    }



    public void setRoutes(List<RouteEntry> routeEntries){
        mRouteEntries = routeEntries;
        notifyDataSetChanged();
    }



    class RouteViewHolder extends RecyclerView.ViewHolder {

        TextView routeTitleMain;

        public RouteViewHolder( View itemView) {
            super(itemView);
            routeTitleMain = (TextView)itemView.findViewById(R.id.route_name_main);
        }
    }


}
