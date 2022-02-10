package com.example.animalsqualifier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    List<AuthUI.IdpConfig> provider;
    int AuthUI_Request_Code=1001;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getSupportActionBar().hide();

        provider= Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
        pb=findViewById(R.id.pb);


    }

    public void proceedLogin(View view) {
        pb.setVisibility(View.VISIBLE);
        gotoSignInMethods();
    }

    private void gotoSignInMethods() {

        Intent intent= AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false).
                setAvailableProviders(provider).setTheme(R.style.Theme_AnimalsQualifier).build();

        startActivityForResult(intent,AuthUI_Request_Code);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==AuthUI_Request_Code){
            if (resultCode==RESULT_OK){
                pb.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();

            }
            else {

                IdpResponse response= IdpResponse.fromResultIntent(data);
                if (response==null){
                    pb.setVisibility(View.GONE);
                    Toast.makeText(this, "you have canceled tht sign in process", Toast.LENGTH_SHORT).show();

                }
                else {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(this, response.getError().toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    public void changeLang(View view) {
        String[] listItems={"English","עברית"};
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Language");
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i==0){
                    setLocale("en");
                    recreate();

                }else   if (i==1){
                    setLocale("iw");
                    recreate();
                }

                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();
    }

    private void setLocale(String lang) {

        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

    }
}