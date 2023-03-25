package com.example.myquizapp;

import static com.example.myquizapp.DbQuery.g_selected_cat_index;
import static com.example.myquizapp.DbQuery.loadquestions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StartTestActivity extends AppCompatActivity {

    private TextView catName,testNo,totalQ,bestScore,time;
    private Button startTestBtn;
    private ImageView backBtn;
    private Dialog progressDialog;
    private TextView dialogText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        init();
        progressDialog = new Dialog(StartTestActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText =progressDialog.findViewById(R.id.dialogText);
        dialogText.setText("Loading ......" );

        progressDialog.show();
        loadquestions(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                setData();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(StartTestActivity.this, "Something went wrong ! Please ty again", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void init()
    {
        catName = findViewById(R.id.st_cat_name);
        testNo = findViewById(R.id.st_test_no);
        totalQ =findViewById(R.id.st_totsl_ques);
        bestScore = findViewById(R.id.st_best_score);
        time = findViewById(R.id.st_time);
        startTestBtn = findViewById(R.id.start_testBtn);
        backBtn = findViewById(R.id.st_backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTestActivity.this.finish();
            }
        });

        startTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartTestActivity.this,QuestionActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
    private void setData()
    {
        catName.setText(DbQuery.g_catList.get(g_selected_cat_index).getName());
        testNo.setText("Test No. "+String.valueOf(DbQuery.g_selected_test_index+1));
        totalQ.setText(String.valueOf(DbQuery.g_question_list.size()));
        bestScore.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTopScore()));
        time.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime()));
    }
}