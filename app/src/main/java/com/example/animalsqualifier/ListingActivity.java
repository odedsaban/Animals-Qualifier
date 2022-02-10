package com.example.animalsqualifier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static java.security.AccessController.getContext;

public class ListingActivity extends AppCompatActivity {
RecyclerView animalsRv;
AnimalsAdapter adapter;
String type;
    EditText searchEt;
    RelativeLayout mainRl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        mainRl=findViewById(R.id.mainRl);
        animalsRv=findViewById(R.id.animalsRv);
        searchEt=findViewById(R.id.searchEt);
        animalsRv.setLayoutManager(new GridLayoutManager(this,3));

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            type=bundle.getString("type");
            if (type.equals("AirAnimals")){
                mainRl.setBackgroundResource(R.drawable.airbg);
            }else  if (type.equals("LandAnimals")){
                mainRl.setBackgroundResource(R.drawable.landbg);
            }
            else  if (type.equals("WaterAnimals")){
                mainRl.setBackgroundResource(R.drawable.waterbg);
            }
            else  if (type.equals("InsectsAnimals")){
                mainRl.setBackgroundResource(R.drawable.insectbg);
            }

        }





        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text=s.toString().toLowerCase();
                if (text.length()>0)
                    processSearch(text);
                else {
                   showAllData();
                   adapter.startListening();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void processSearch(String text) {

        PagedList.Config config=new PagedList.Config.Builder().setInitialLoadSizeHint(15)
                .setPageSize(15).build();
        Query query= FirebaseFirestore.getInstance().collection(type).orderBy("search").startAt(text).endAt(text + "\uf8ff");

        FirestorePagingOptions<AnimalModel> options = new FirestorePagingOptions.Builder<AnimalModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, AnimalModel.class)
                .build();


        adapter=new AnimalsAdapter(options,getApplicationContext());
        animalsRv.setAdapter(adapter);
        adapter.startListening();


    }

    private void showAllData() {
        PagedList.Config config=new PagedList.Config.Builder().setInitialLoadSizeHint(15)
                .setPageSize(15).build();
        Query query= FirebaseFirestore.getInstance().collection(type).orderBy("time", Query.Direction.DESCENDING);

        FirestorePagingOptions<AnimalModel> options = new FirestorePagingOptions.Builder<AnimalModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, AnimalModel.class)
                .build();


        adapter=new AnimalsAdapter(options,getApplicationContext());
        animalsRv.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
       showAllData();

    }

}