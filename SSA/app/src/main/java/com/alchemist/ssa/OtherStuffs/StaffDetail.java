package com.alchemist.ssa.OtherStuffs;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.alchemist.ssa.R;

import java.util.ArrayList;
import java.util.List;

public class StaffDetail extends AppCompatActivity implements SearchInterface{
    private RecyclerView staffRecycleView;
    private List<StaffModel> staffModelList=new ArrayList<>();
    private StaffAdapter staffAdapter;
    private Button searchButton;
    private SearchInterface searchInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_detail);

        staffRecycleView=findViewById(R.id.staffRecycleView);
        Toolbar toolbar= findViewById(R.id.resultToolBar);
        setSupportActionBar(toolbar);


//        searchButton=findViewById(R.id.searchButton);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                staffModelList.clear();
//                loadResult();
//            }
//        });

        staffAdapter=new StaffAdapter(getApplicationContext(),staffModelList);
       // staffAdapter.searchEnableInterface(this);

        loadResult();
        addSearchListener(this);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        staffRecycleView.setLayoutManager(linearLayoutManager);
        staffRecycleView.setAdapter(staffAdapter);
    }


    //code if required in future BitmapFactory.decodeResource(getResources(),R.drawable.category4)

    private void loadResult() {

        StaffModel staffModel=new StaffModel("Papu",981411111,"primary");
        staffModelList.add(staffModel);

        staffModel=new StaffModel("RKC",98114111,"secondary");
        staffModelList.add(staffModel);

        staffModel=new StaffModel("BKM",98114111,"pre-primary");
        staffModelList.add(staffModel);

        staffModel=new StaffModel("KAC",98114111,"secondary");
        staffModelList.add(staffModel);
        staffAdapter.notifyDataSetChanged();

    }


    public String getSpace(String data){
        int length=data.length();
        //10 space character
        for(int i=0;i<(10-length);i++){
            data=data+"x";
        }


        return data;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.search_bar,menu);
        MenuItem menuItem=menu.findItem(R.id.search);



        SearchManager searchManager=(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(),"sssss",Toast.LENGTH_SHORT).show();
                 //staffAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchInterface.onSearch(newText);
                return true;
            }
        });
        return true;
    }


    @Override
    public void onSearch(String newText) {
        newText=newText.toLowerCase();
        ArrayList<StaffModel> newlinkModels=new ArrayList<>();

        for(StaffModel linkModel: newlinkModels){
            //searching by name as well as category
            String teacherName=linkModel.getTeacherName();
            String level=linkModel.getLevel();
            if(teacherName.contains(newText)||level.contains(newText)){
                newlinkModels.add(linkModel);
            }


        }

        staffAdapter.setFilter(newlinkModels);
    }
    public void addSearchListener(SearchInterface searchInterface) {
        this.searchInterface=searchInterface;
    }
}
