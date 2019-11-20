package com.gmail.all.vanyadubik.testgbksoft.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.all.vanyadubik.testgbksoft.R;
import com.gmail.all.vanyadubik.testgbksoft.model.Point;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PointListAdapter extends RecyclerView.Adapter<PointListAdapter.PointViewHolder> {

    private Context mContext;
    private List<Point> list;
    private LayoutInflater layoutInflater;
    private ClickListener clickListener;

    public PointListAdapter(Context mContext, ClickListener clickListener) {
        this.mContext = mContext;
        this.list = new ArrayList<>();
        this.clickListener = clickListener;
        this.layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<Point> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public class PointViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener{

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.latitude)
        TextView latitude;
        @BindView(R.id.longitude)
        TextView longitude;

        public PointViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickListener.onItemClick(position, getSelectedItem(position));
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            clickListener.onItemLongClick(position, getSelectedItem(position));
            return true;
        }
    }

    @NonNull
    @Override
    public PointViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PointViewHolder(layoutInflater.inflate(R.layout.point_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PointViewHolder viewHolder, int i) {

        Point selectedItem = getSelectedItem(i);
        if (selectedItem != null) {

            viewHolder.name.setText(selectedItem.getName());
            viewHolder.latitude.setText(String.valueOf(selectedItem.getLatitude()));
            viewHolder.longitude.setText(String.valueOf(selectedItem.getLongitude()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private Point getSelectedItem(int position) {
        return list.get(position);
    }

    public interface ClickListener {
        void onItemClick(int position, Point selectedItem);
        void onItemLongClick(int position, Point selectedItem);
    }

}
