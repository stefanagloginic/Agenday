package com.example.stefana.agenday;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    //new
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Context context;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //now I will create the lists for the home schedule
        //a recyclerView requires a layout and an adapter.

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context)); //might want to use itemanimator soon (will allow swipe feature)

        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.dividerdrawable);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        recyclerView.addItemDecoration(dividerItemDecoration);



        //now I will specify the adapter..for which I will make a class
        ArrayList<FakeCard> fakecard = new ArrayList<>();
        initializeData(fakecard);

        adapter =  new MyAdapter(fakecard);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initializeData(ArrayList<FakeCard> fakecards){
        fakecards.add(new FakeCard("Coldplay playing in CA, LA on Monday","Concert"));
        fakecards.add(new FakeCard("Enjoy our Greek cuisine in San Fran","Greek Restaurant"));
        fakecards.add(new FakeCard("Torture yourself at the stairs of hell in Culver City","Hike"));
        fakecards.add(new FakeCard("Enjoy paradise", "Vacation"));

    }

}
