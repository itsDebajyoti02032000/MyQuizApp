package com.example.myquizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText name,email,pass,confirmPass;
    private Button signUpBtn;
    private ImageView backbtn;
    private FirebaseAuth mAuth;
    private String emailStr,passStr,confirmPassStr,nameStr;
    private Dialog progressDialog;
    private TextView dialogText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.username);
        email = findViewById(R.id.emailID);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirm_pass);
        signUpBtn = findViewById(R.id.signupbtn);
        backbtn  = findViewById(R.id.backarrow);

        progressDialog = new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText =progressDialog.findViewById(R.id.dialogText);
        dialogText.setText("Registering user......" );

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                SignUpActivity.this.finish();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    signupNewUser();
                }
            }
        });

    }
    private boolean validate(){
        nameStr= name.getText().toString().trim();
        passStr = pass.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        confirmPassStr = confirmPass.getText().toString().trim();

        if(nameStr.isEmpty()){
            name.setError("Enter Your Name");
            return false;
        }
        if(emailStr.isEmpty()){
            email.setError("Enter Your EmailID");
            return false;
        }
        if(passStr.isEmpty()){
            pass.setError("Enter Password");
            return false;
        }
        if(confirmPassStr.isEmpty()){
            confirmPass.setError("Enter Password");
            return false;
        }
        if(passStr.compareTo(confirmPassStr) != 0){
            Toast.makeText(this, "Password and Connfirm Password should be same", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signupNewUser() {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailStr,passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

                            DbQuery.createUserData(emailStr,nameStr,new MyCompleteListener(){

                                @Override
                                public void onSuccess() {

                                    DbQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            progressDialog.dismiss();
                                            Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                                            startActivity(i);
                                            SignUpActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(SignUpActivity.this, "Something went wrong ! Please try again later !", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });

                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(SignUpActivity.this, "Something went wrong ! Please try again later !", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed.Try Again! ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}



