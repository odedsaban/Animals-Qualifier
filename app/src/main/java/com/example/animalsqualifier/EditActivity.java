package com.example.animalsqualifier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {
    String name,desc,id,image,type;

    EditText nameEt,descEt;

    FloatingActionButton addFab;
    CircleImageView profileIv;
    Uri profileImageUri=null;
    ProgressBar pb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        nameEt=findViewById(R.id.nameEt);
        descEt=findViewById(R.id.descEt);
        addFab=findViewById(R.id.changeDp_Fab);
        profileIv=findViewById(R.id.profile_Iv);
        pb=findViewById(R.id.pb);


        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){

            name = bundle.getString("name");
            desc = bundle.getString("desc");
            type = bundle.getString("type");
            id = bundle.getString("id");
            image=bundle.getString("image");



            nameEt.setText(name);
            descEt.setText(desc);
            Glide.with(getApplicationContext()).load(bundle.getString("image"))
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profileIv);


        }

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                permissionCheck();

            }
        });


    }

    private void permissionCheck() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            askPermission();
        }
        else{


            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Select Image"), 1);

        }
    }

    private void askPermission(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            Uri imgUri = data.getData();

            profileIv.setImageURI(imgUri);
            profileImageUri=imgUri;
        }


    }

    public void saveData(View view) {

        if (nameEt.getText().toString().length()<1){
            nameEt.setError("This field must be filled");
            return;
        }
        if (descEt.getText().toString().length()<1){
            descEt.setError("This field must be filled");
            return;
        }

        if (profileImageUri==null){

            pb.setVisibility(View.VISIBLE);

            Map map=new HashMap();
            map.put("name",nameEt.getText().toString());
            map.put("search",nameEt.getText().toString().toLowerCase());
            map.put("desc",descEt.getText().toString());
            map.put("type",type);
            map.put("image",image);


            FirebaseFirestore.getInstance().collection(type+"Animals").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    pb.setVisibility(View.GONE);
                    Toast.makeText(EditActivity.this, "Animal edited successfully", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    pb.setVisibility(View.GONE);
                    Toast.makeText(EditActivity.this, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }else {
            uploadPI();
        }




    }

    private void uploadPI() {

        pb.setVisibility(View.VISIBLE);

        String idd=System.currentTimeMillis()+".jpg";
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("AnimalsImages");

        storageReference.child(idd).putFile(profileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.child(idd).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String  profileImageLink=uri.toString();

                        Map map=new HashMap();
                        map.put("name",nameEt.getText().toString());
                        map.put("search",nameEt.getText().toString().toLowerCase());
                        map.put("desc",descEt.getText().toString());
                        map.put("type",type);
                        map.put("image",profileImageLink);


                        FirebaseFirestore.getInstance().collection(type+"Animals").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                pb.setVisibility(View.GONE);
                                Toast.makeText(EditActivity.this, "Animal edited successfully", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                pb.setVisibility(View.GONE);
                                Toast.makeText(EditActivity.this, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(EditActivity.this, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pb.setVisibility(View.GONE);
                Toast.makeText(EditActivity.this, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}