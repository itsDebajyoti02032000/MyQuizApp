package com.example.myquizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    private ImageView appLogo;
    private TextView appName;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        appLogo = findViewById(R.id.splash_appLogo);
        appName = findViewById(R.id.splash_appName);

        mAuth = FirebaseAuth.getInstance();
        Typeface typeface = ResourcesCompat.getFont(this,R.font.blacklist);
        appName.setTypeface(typeface);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.myanim);
        appName.setAnimation(anim);
        DbQuery.g_firestore = FirebaseFirestore.getInstance();

        new Thread() {
            @Override
            public void run()
            {
                try{
                    sleep(3000);

                }catch(InterruptedException e){
                    e.printStackTrace();
                }

                if(mAuth.getCurrentUser()!=null)
                {
                    DbQuery.loadData(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(SplashActivity.this, "Something went wrong ! Please try again later !", Toast.LENGTH_SHORT).show();
                        }
                    });



                }
                else
                {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }
        }.start();

    }
}