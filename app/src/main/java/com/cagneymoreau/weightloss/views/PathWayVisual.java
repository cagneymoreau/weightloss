package com.cagneymoreau.weightloss.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.cagneymoreau.weightloss.MainActivity;
import com.cagneymoreau.weightloss.R;
import com.cagneymoreau.weightloss.logic.GameController;
import com.cagneymoreau.weightloss.logic.Level;
import com.cagneymoreau.weightloss.logic.RecyclerTouchListener;
import com.cagneymoreau.weightloss.views.recycleviewpathway.Adapter;

import java.util.ArrayList;


/**
 * Home Screen
 * Visualization of each level so the user can search completed and future levels
 */

public class PathWayVisual extends Fragment {


    private final static String TAG = "PathwayVisual";
    View frag;
    GameController gamecontroller;

    RecyclerView recyclerView;
    Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Level> levelsList;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        frag = inflater.inflate(R.layout.pathway_fragment, container, false);

        MainActivity activity = (MainActivity) getActivity();
        gamecontroller = activity.getGameController();
        buildRecycleView();

        return frag;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void buildRecycleView()
    {
        Fragment parent = this;

        levelsList = gamecontroller.getLevelsList();

        recyclerView = frag.findViewById(R.id.pathway_recycleview);
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(levelsList, this);
        recyclerView.setAdapter(adapter);

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y) {
                //new Level_Dialog(position,parent).show(getChildFragmentManager(), "level choice dialog");
            }

            @Override
            public void onLongClick(View view, int position, float x, float y) {
                //do nothing
            }
        }));


    }



    public void popUpReturn(int level, int choice)
    {
        // TODO: 12/13/2021  paywall check for level 2

        if ((level > 1) && (!gamecontroller.allowAccess(this))){
            return;
        }


        Bundle b = new Bundle();
        b.putInt("level", level);
        Navigation.findNavController(frag).navigate(choice, b);

    }

    public void updateRecycleView()
    {
        adapter.notifyDataSetChanged();
    }





}
