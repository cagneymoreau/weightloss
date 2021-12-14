package com.cagneymoreau.weightloss.views.recycleviewpathway;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagneymoreau.weightloss.R;
import com.cagneymoreau.weightloss.logic.Level;
import com.cagneymoreau.weightloss.views.PathWayVisual;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ArrayList<Level> levels;
    PathWayVisual pathWayVisual;


    public Adapter(ArrayList<Level> dataField, PathWayVisual pathWayVisual)
    {
        levels = dataField;
        this.pathWayVisual = pathWayVisual;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.view_card, parent, false);
        viewHolder = new ViewHolder(v, pathWayVisual);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

       ViewHolder vv = (ViewHolder) holder;

       vv.setVals(levels.get(position));

    }


    @Override
    public int getItemCount() {
        return levels.size();
    }



}
