package com.example.myquizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaCasException;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {
    private TextView scoreTV,timeTV,totalQuesTV,correctTV,wrongTV,unattemptedTV;
    private Button leaderB,reAttemptB,viewAnsB;
    private long timeTaken;
    private Dialog progressDialog;
    private TextView dialogText;
    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Toolbar toolbar=findViewById(R.id.toolbarScoreActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Dialog(ScoreActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText =progressDialog.findViewById(R.id.dialogText);
        dialogText.setText("Loading ......" );
        progressDialog.show();

        init();
        loadData();

        leaderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewAnsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ScoreActivity.this,AnswerActivity.class);
                startActivity(i);
            }
        });

        reAttemptB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reAttempt();
            }
        });

        saveResult();
    }
    private void init()
    {
        scoreTV = findViewById(R.id.score);
        timeTV = findViewById(R.id.time);
        totalQuesTV = findViewById(R.id.totalQuestions);
        correctTV = findViewById(R.id.noOfCorrect_answer);
        wrongTV = findViewById(R.id.noOfWrong_answer);
        unattemptedTV = findViewById(R.id.noOfUnattempted);
        leaderB = findViewById(R.id.leaderBoardBtn);
        reAttemptB = findViewById(R.id.reAttemptBtn);
        viewAnsB = findViewById(R.id.viewAnswerBtn);
    }
    private void loadData(){
       int correctQ=0,wrongQ=0,unattempt=0;
       for(int i =0;i<DbQuery.g_question_list.size();i++){
           if(DbQuery.g_question_list.get(i).getSelectedAns()==-1)
           {
               unattempt++;
           }
           else
           {
               if(DbQuery.g_question_list.get(i).getSelectedAns()==DbQuery.g_question_list.get(i).getCorrectAns())
               {
                   correctQ++;
               }
               else
               {
                   wrongQ++;
               }
           }
       }
       correctTV.setText(String.valueOf(correctQ));
       wrongTV.setText(String.valueOf(wrongQ));
       unattemptedTV.setText(String.valueOf(unattempt));
       totalQuesTV.setText(String.valueOf(DbQuery.g_question_list.size()));
        finalScore = (correctQ*100)/DbQuery.g_question_list.size();
       scoreTV.setText(String.valueOf(finalScore));

       timeTaken = getIntent().getLongExtra("TIME_TAKEN",0);
        String time = String.format("%02d : %02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))
        );
        timeTV.setText(time);
    }

    private void reAttempt(){
        for(int i =0;i<DbQuery.g_question_list.size();i++)
        {
            DbQuery.g_question_list.get(i).setSelectedAns(-1);
            DbQuery.g_question_list.get(i).setStatus(DbQuery.NOT_VISITED);
        }
        Intent i = new Intent(ScoreActivity.this,StartTestActivity.class);
        startActivity(i);
        finish();
    }
    private void saveResult(){
        DbQuery.saveResult(finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(ScoreActivity.this, "Something went wrong ! Please try again later", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            ScoreActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}