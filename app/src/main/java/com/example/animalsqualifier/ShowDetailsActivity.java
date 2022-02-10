package com.example.animalsqualifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowDetailsActivity extends AppCompatActivity {
TextView nameTv,descTv,typeTv;
CircleImageView profile_Iv;
String image;
String name,desc,type,id, typeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        nameTv=findViewById(R.id.nameTv);
        descTv=findViewById(R.id.descTv);
        typeTv=findViewById(R.id.typeTv);
        profile_Iv=findViewById(R.id.profile_Iv);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){

            name = bundle.getString("name");
            desc = bundle.getString("desc");
            type = bundle.getString("type");
            id = bundle.getString("id");

            nameTv.setText(getString(R.string.name)+bundle.getString("name"));
            String typeIs = getString(R.string.type);
            if(typeIs.charAt(0) != 84) {
                switch(bundle.getString("type")) {
                    case "Air":
                        typeText = "חיות בעלות כנף";
                        break;
                    case "Land":
                        typeText = "חיות יבשה";
                        break;
                    case "Water":
                        typeText = "חיות תת ימיות";
                    case "Insects":
                        typeText = "חרקים";
                    default:
                        break;
                }
                typeTv.setText(getString(R.string.type)+typeText);
            } else {
                typeTv.setText(getString(R.string.type)+bundle.getString("type")+" Animal");
            }
            descTv.setText(getString(R.string.description)+bundle.getString("desc"));
            image=bundle.getString("image");
            Glide.with(getApplicationContext()).load(bundle.getString("image"))
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile_Iv);



        }

    }

    public void shareData(View view) {

        String share=nameTv.getText().toString()+"\n"+typeTv.getText().toString()+"\n"+descTv.getText().toString()+"\n"+"Image : "+image;
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, share);
        startActivity(Intent.createChooser(intent, "Share using"));
    }

    public void editData(View view) {

        Intent intent=new Intent(getApplicationContext(),EditActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("desc",desc);
        intent.putExtra("type",type);
        intent.putExtra("image",image);
        intent.putExtra("id",id);
        startActivity(intent);
        finish();

    }
}