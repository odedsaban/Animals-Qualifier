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

public class AddNewAnimalActivity extends AppCompatActivity {
EditText nameEt,descEt;
RadioGroup typeRg;
String type="Air";
FloatingActionButton addFab;
CircleImageView profileIv;
Uri profileImageUri=null;
ProgressBar pb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_animal);

        nameEt=findViewById(R.id.nameEt);
        descEt=findViewById(R.id.descEt);
        typeRg=findViewById(R.id.typeRg);
        addFab=findViewById(R.id.changeDp_Fab);
        profileIv=findViewById(R.id.profile_Iv);
        pb=findViewById(R.id.pb);



        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                permissionCheck();

            }
        });

        typeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.airRb){
                    type="Air";
                }else  if (i==R.id.landRb){
                    type="Land";
                }
                else  if (i==R.id.waterRb){
                    type="Water";
                }
                else  if (i==R.id.insectsRb){
                    type="Insects";
                }
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
            Toast.makeText(this, "Insert animal image", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadPI();


    }

    private void uploadPI() {

        pb.setVisibility(View.VISIBLE);

        String id=System.currentTimeMillis()+".jpg";
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("AnimalsImages");

        storageReference.child(id).putFile(profileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.child(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                      String  profileImageLink=uri.toString();

                        Map map=new HashMap();
                        map.put("name",nameEt.getText().toString());
                        map.put("search",nameEt.getText().toString().toLowerCase());
                        map.put("desc",descEt.getText().toString());
                        map.put("type",type);
                        map.put("image",profileImageLink);
                        map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        map.put("time", String.valueOf(System.currentTimeMillis()));

                        FirebaseFirestore.getInstance().collection(type+"Animals").document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                pb.setVisibility(View.GONE);
                                Toast.makeText(AddNewAnimalActivity.this, "New animal saved successfully", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                pb.setVisibility(View.GONE);
                                Toast.makeText(AddNewAnimalActivity.this, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(AddNewAnimalActivity.this, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pb.setVisibility(View.GONE);
                Toast.makeText(AddNewAnimalActivity.this, "Failed : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }




}