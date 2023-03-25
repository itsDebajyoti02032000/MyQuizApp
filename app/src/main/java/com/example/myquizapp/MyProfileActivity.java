package com.example.myquizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MyProfileActivity extends AppCompatActivity {
    private EditText name,email,phone ;
    private LinearLayout editB,btnLayout;
    private TextView profileText;
    private Button saveB, cancelB;
    private Dialog progressDialog;
    private String nameStr,phoneStr;
    private TextView dialogText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileText = findViewById(R.id.nameMyProfile);
        name =findViewById(R.id.myProfile_name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phn_num);
        editB = findViewById(R.id.editBtn);
        saveB = findViewById(R.id.saveBtn);
        cancelB = findViewById(R.id.cancelBtn);
        btnLayout = findViewById(R.id.btnLayout);

        progressDialog = new Dialog(MyProfileActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText =progressDialog.findViewById(R.id.dialogText);
        dialogText.setText("Updating Data ......" );

        disableEditing();

        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableEditing();
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableEditing();
            }
        });
        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    saveData();
                    disableEditing();
                }
            }
        });
    }

    private void disableEditing(){
        name.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);
        btnLayout.setVisibility(View.GONE);
        name.setText(DbQuery.myProfile.getName());
        email.setText(DbQuery.myProfile.getEmail());

        if(DbQuery.myProfile.getPhone()!=null)
        {
            phone.setText(DbQuery.myProfile.getPhone());
        }
        String profile_name = DbQuery.myProfile.getName();
        profileText.setText(profile_name.toUpperCase().substring(0,1));

    }
    private void enableEditing()
    {
        name.setEnabled(true);
        //email.setEnabled(false);
        phone.setEnabled(true);
        btnLayout.setVisibility(View.VISIBLE);

    }

    private boolean validate(){
        nameStr= name.getText().toString().trim();
        phoneStr = phone.getText().toString().trim();

        if(nameStr.isEmpty()){
            name.setError("Enter Your Name");
            return false;
        }
        if(phoneStr.isEmpty()){
            phone.setError("Enter Phone number");
            return false;
        }
        if(!phoneStr.isEmpty())
        {
            if(!(phoneStr.length()==10) && (TextUtils.isDigitsOnly(phoneStr)))
            {
                phone.setError("Enter Valid Phone number");
                return false;
            }
        }
        return true;
    }

    private void saveData(){

        progressDialog.show();
        if(phoneStr.isEmpty())
            phoneStr =null;
        DbQuery.saveProfileData(nameStr, phoneStr, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MyProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(MyProfileActivity.this, "Something went wrong !Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            MyProfileActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}