package com.gmail.all.vanyadubik.testgbksoft.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.gmail.all.vanyadubik.testgbksoft.R;
import com.gmail.all.vanyadubik.testgbksoft.adapter.PointListAdapter;
import com.gmail.all.vanyadubik.testgbksoft.model.Point;
import com.gmail.all.vanyadubik.testgbksoft.ui.activity.MainActivity;

import java.util.List;

public class ListFragment extends Fragment {

    @BindView(R.id.container)
    ConstraintLayout container;

    @BindView(R.id.text_no_data)
    TextView textNoData;

    @BindView(R.id.list_points)
    RecyclerView listPoints;
    private ListViewModel listViewModel;
    private PointListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listViewModel =
                ViewModelProviders.of(this).get(ListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, root);

        adapter = new PointListAdapter(getActivity(), new PointListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, Point selectedItem) {
                ((MainActivity)getActivity()).showMapView(selectedItem);
            }

            @Override
            public void onItemLongClick(int position, Point selectedItem) {
                if(selectedItem!=null) {
                    listViewModel.detelePoint(selectedItem);
                }
            }
        });
        listPoints.setLayoutManager(new LinearLayoutManager(getActivity()));
        listPoints.setAdapter(adapter);

        listViewModel.getList().observe(this, (points)->{
            updateUi(points);

        });
        return root;
    }

    private void updateUi(List<Point> points){
        ConstraintSet set = new ConstraintSet();
        set.clone(container);
        if(points == null || points.size()==0){
            set.connect(R.id.text_no_data, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            set.connect(R.id.text_no_data, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

            set.clear(R.id.list_points, ConstraintSet.BOTTOM);
            set.connect(R.id.list_points, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        }else{
            set.clear(R.id.text_no_data, ConstraintSet.TOP);
            set.connect(R.id.text_no_data, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP);

            set.connect(R.id.list_points, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            set.connect(R.id.list_points, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

            adapter.setList(points);
        }
        TransitionManager.beginDelayedTransition(container);
        set.applyTo(container);
    }
}