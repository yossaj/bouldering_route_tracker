package com.example.boulderjournal;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boulderjournal.data.RouteContract;

import static com.example.boulderjournal.data.RouteContract.RouteEntry.COLUMN_ROUTE_NAME;

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.RouteViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public CustomCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CustomCursorAdapter.RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.route_input_layout, parent, false);

        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(RouteContract.RouteEntry._ID);
        int routeNameIndex = mCursor.getColumnIndex(COLUMN_ROUTE_NAME);
        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        String routeName = mCursor.getString(routeNameIndex);

        holder.itemView.setTag(id);
        holder.routeTitleMain.setText(routeName);

    }


    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        int count = mCursor.getCount();
        return count;
    }

    public Cursor swapCursor(Cursor c){
        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;

    }




    class RouteViewHolder extends RecyclerView.ViewHolder {

        TextView routeTitleMain;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            routeTitleMain = (TextView)itemView.findViewById(R.id.route_name_main);
        }
    }
}
