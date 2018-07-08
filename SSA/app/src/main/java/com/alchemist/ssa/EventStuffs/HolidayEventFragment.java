package com.alchemist.ssa.EventStuffs;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.alchemist.ssa.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashish Alton on 7/5/2018.
 */

public class HolidayEventFragment extends Fragment {
    List<EventModel> list=new ArrayList<>();
    RecyclerView recyclerView;
    EventAdapter eventAdapter;
    private boolean isLoading=false;
    private int thresholdLoad=2;
    ProgressBar progressBar;
    private int count=0;


    public HolidayEventFragment(){
        //default constuctor for this class
        System.out.print("The system is created");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v=inflater.inflate(R.layout.holidayfragment,container,false);
        eventAdapter=new EventAdapter(getActivity(),list);
        recyclerView=v.findViewById(R.id.holidayList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        progressBar=v.findViewById(R.id.progressLoad);
        DividerItemDecoration decoration=new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        // DividerItemDecoration decoration=new DividerItemDecoration(getActivity(),linearLayoutManager.getOrientation());

        setData();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(eventAdapter);
      /* This section is for loading the items in a constant manner , 4 at a time */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView,dx,dy);
                int totalCount=recyclerView.getLayoutManager().getItemCount();
                int lastCount= ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                Log.d("size",list.size()+"");
                Log.d("totalCount",totalCount+"");
                Log.d("lastCount",lastCount+"");
                Log.d("dx",dx+"");
                Log.d("dy",dy+"");

                if(dy<0){
                    isLoading=true;
                    loadDynamically(-1,true);

                }
                if(!isLoading && totalCount<=(lastCount+thresholdLoad)){

                    Log.d("dynamic","Dynamic loading enabled");
                    loadDynamically(totalCount,true);
                }
                isLoading=true;

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return v;
    }
    public void setData(){
        list.add(new EventModel("Maha puja","2015-02-25","maha puja of the year", BitmapFactory.decodeResource(getResources(),R.drawable.category4)));
        list.add(new EventModel("Maha puja","2015-02-25","maha puja of the year", BitmapFactory.decodeResource(getResources(),R.drawable.category4)));
        list.add(new EventModel("Maha puja","2015-02-25","maha puja of the year", BitmapFactory.decodeResource(getResources(),R.drawable.category4)));
        list.add(new EventModel("Maha puja","2015-02-25","maha puja of the year", BitmapFactory.decodeResource(getResources(),R.drawable.category4)));
        list.add(new EventModel("Maha puja","2015-02-25","maha puja of the year", BitmapFactory.decodeResource(getResources(),R.drawable.category4)));
        list.add(new EventModel("Maha puja","2015-02-25","maha puja of the year", BitmapFactory.decodeResource(getResources(),R.drawable.category4)));
        list.add(new EventModel("Maha puja","2015-02-25","maha puja of the year", BitmapFactory.decodeResource(getResources(),R.drawable.category4)));
        list.add(new EventModel("Maha puja","2015-02-25","maha puja of the year", BitmapFactory.decodeResource(getResources(),R.drawable.category4)));
        eventAdapter.notifyDataSetChanged();

    }
    public void loadDynamically(int offset,boolean dynamicLoad){

        Log.d("offset Value",offset+"");
        Log.d("arraySize",list.size()+"");
        Log.d("loading",dynamicLoad+"");
        if(!dynamicLoad){
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);


        }
        if(isLoading){
            progressBar.setVisibility(View.GONE);
            progressBar.setIndeterminate(false);
        }
        //On response from the server dynamicLoad=true

        if(offset==list.size()){
            isLoading=true;
        }

        //on response from the server
//        for(int i=0;i<5;i++) {
//            count++;
//            list.add(new EventModel("THird puja", "2015-02-25", "third puja of the year", BitmapFactory.decodeResource(getResources(), R.drawable.category4)));
//            list.add(new EventModel("Puja puja", "2015-02-25", "kaag puja of the year", BitmapFactory.decodeResource(getResources(), R.drawable.category4)));
//            eventAdapter.notifyDataSetChanged();
//            if(count==4)
//                isLoading=false;
//
//
//        }


        //setData2();
        //eventAdapter.notifyDataSetChanged();




    }

    private boolean performNegation() {
        return false;
    }

    public void setData2(){
        list.add(new EventModel("THird puja","2015-02-25","third puja of the year", BitmapFactory.decodeResource(getResources(),R.drawable.category4)));
        list.add(new EventModel("Puja puja","2015-02-25", "kaag puja of the year", BitmapFactory.decodeResource(getResources(),R.drawable.category4)));
        list.add(new EventModel("GAI","2015-02-25","tihar puja of the year", BitmapFactory.decodeResource(getResources(),R.drawable.category4)));
        list.add(new EventModel("Maha puja","2015-02-25","mah puja of the year", BitmapFactory.decodeResource(getResources(),R.drawable.category4)));
        eventAdapter.notifyDataSetChanged();


    }
}
