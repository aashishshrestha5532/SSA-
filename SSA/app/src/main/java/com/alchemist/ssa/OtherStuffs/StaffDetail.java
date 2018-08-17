package com.alchemist.ssa.OtherStuffs;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.alchemist.ssa.R;

import java.util.ArrayList;
import java.util.List;

public class StaffDetail extends AppCompatActivity {
    private RecyclerView staffRecycleView;
    private List<StaffModel> staffModelList = new ArrayList<>();
    private StaffAdapter staffAdapter;
    private Button searchButton;
    private SearchInterface searchInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_detail);

        staffRecycleView = findViewById(R.id.staffRecycleView);
        Toolbar toolbar = findViewById(R.id.resultToolBar);
        setSupportActionBar(toolbar);


//        searchButton=findViewById(R.id.searchButton);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                staffModelList.clear();
//                loadResult();
//            }
//        });

        staffAdapter = new StaffAdapter(getApplicationContext(), staffModelList);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(getApplicationContext(), "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
               // Toast.makeText(getApplicationContext(), "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                AlertDialog.Builder builder1 = new AlertDialog.Builder(StaffDetail.this);
                builder1.setTitle("Phone call")
                        .setMessage("Do you sure want to make call?")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getApplicationContext(),list.get(viewHolder.getAdapterPosition()).getEvent_id()+"",Toast.LENGTH_SHORT).show();
                                int position = viewHolder.getAdapterPosition();
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                                callIntent.setData(Uri.parse("tel:"+staffModelList.get(position).getPhone()));
                                startActivity(callIntent);


                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        staffAdapter.notifyDataSetChanged();
                    }
                }).show();


            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(staffRecycleView);
       // staffAdapter.searchEnableInterface(this);

        loadResult();
        searchInterface=new SearchInterface() {
            @Override
            public void onSearch(String queryText) {
                String smallText=queryText.toLowerCase();
                ArrayList<StaffModel> newlinkModels=new ArrayList<>();

                for(StaffModel linkModel: staffModelList){
                    //searching by name as well as category
                    String teacherName=linkModel.getTeacherName();
                    //String level=linkModel.getLevel();
                    if(teacherName.contains(queryText)|| teacherName.toLowerCase().contains(smallText)) {
                        newlinkModels.add(linkModel);
                    }


                }
                staffAdapter.setFilter(newlinkModels);
            }
        };


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        staffRecycleView.setLayoutManager(linearLayoutManager);
        staffRecycleView.setAdapter(staffAdapter);
//        staffModelList.clear();
//        staffAdapter.notifyDataSetChanged();
    }


    //code if required in future BitmapFactory.decodeResource(getResources(),R.drawable.category4)

    private void loadResult() {

        StaffModel staffModel=new StaffModel("Papu",981411111,"principal");
        staffModelList.add(staffModel);

        staffModel=new StaffModel("RKC",98114111,"teacher");
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
        searchView.setQueryHint("Enter Staff name");
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



    public void addSearchListener(SearchInterface searchInterace) {
        this.searchInterface=searchInterface;
    }
}
